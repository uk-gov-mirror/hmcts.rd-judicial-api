locals {
  preview_vault_name      = join("-", [var.raw_product, "aat"])
  non_preview_vault_name  = join("-", [var.raw_product, var.env])
  key_vault_name          = var.env == "preview" || var.env == "spreview" ? local.preview_vault_name : local.non_preview_vault_name

  s2s_rg_prefix               = "rpe-service-auth-provider"
  s2s_key_vault_name          = var.env == "preview" || var.env == "spreview" ? join("-", ["s2s", "aat"]) : join("-", ["s2s", var.env])
  s2s_vault_resource_group    = var.env == "preview" || var.env == "spreview" ? join("-", [local.s2s_rg_prefix, "aat"]) : join("-", [local.s2s_rg_prefix, var.env])
  vm_availabilty_zones  = [1, 2]
}

data "azurerm_key_vault" "rd_key_vault" {
  name                = local.key_vault_name
  resource_group_name = local.key_vault_name
}

data "azurerm_key_vault" "s2s_key_vault" {
  name                = local.s2s_key_vault_name
  resource_group_name = local.s2s_vault_resource_group
}

data "azurerm_key_vault_secret" "s2s_secret" {
  name          = "microservicekey-rd-judicial-api"
  key_vault_id  = data.azurerm_key_vault.s2s_key_vault.id
}

resource "azurerm_key_vault_secret" "judicial_s2s_secret" {
  name          = "judicial-api-s2s-secret"
  value         = data.azurerm_key_vault_secret.s2s_secret.value
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "vm_password" {
  name          = "vm-password"
  value         = random_string.password.result
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name          = join("-", [var.component, "POSTGRES-USER"])
  value         = module.db-judicial-ref-data.user_name
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name          = join("-", [var.component, "POSTGRES-PASS"])
  value         = module.db-judicial-ref-data.postgresql_password
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name          = join("-", [var.component, "POSTGRES-HOST"])
  value         = module.db-judicial-ref-data.host_name
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name          = join("-", [var.component, "POSTGRES-PORT"])
  value         = "5432"
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name          = join("-", [var.component, "POSTGRES-DATABASE"])
  value         = module.db-judicial-ref-data.postgresql_database
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

module "db-judicial-ref-data" {
  source              = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  product             = var.product
  component           = var.component
  name                = join("-", [var.product, var.component, "postgres-db"])
  location            = var.location
  subscription        = var.subscription
  env                 = var.env
  postgresql_user     = "dbjuddata"
  database_name       = "dbjuddata"
  common_tags         = var.common_tags
  postgresql_version  = var.postgresql_version
}




resource "azurerm_network_interface" "example" {
  name                = "example-nic"
  location            = "uksouth"
  resource_group_name = "rd-aks-pr-test"

  ip_configuration {
    name                          = "internal"
    subnet_id                     = data.azurerm_subnet.vm_subnet.id
    private_ip_address_allocation = "Dynamic"
  }
}

resource "azurerm_windows_virtual_machine" "example" {
  name                = "example-machine"
  resource_group_name = "rd-aks-pr-test"
  location            = "uksouth"
  size                = "Standard_F2"
  admin_username      = "rdUser"
  admin_password      =  random_string.password.result
  network_interface_ids = [
    azurerm_network_interface.example.id,
  ]

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "MicrosoftWindowsServer"
    offer     = "WindowsServer"
    sku       = "2016-Datacenter"
    version   = "latest"
  }
}

data "azurerm_subnet" "vm_subnet" {
  name                 = "aks-01"
  virtual_network_name = "cft-preview-vnet"
  resource_group_name  = "cft-preview-network-rg"
}

resource "azurerm_key_vault_secret" "vm_password" {
  name          = "vm-password"
  value         = random_string.password.result
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}
resource "random_string" "password" {
  length  = 16
  special = true
  upper   = true
  lower   = true
  number  = true
}


