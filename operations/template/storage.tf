resource "azurerm_storage_account" "storage" {
  name                              = "cdcti${var.environment}"
  resource_group_name               = data.azurerm_resource_group.group.name
  location                          = data.azurerm_resource_group.group.location
  account_tier                      = "Standard"
  account_replication_type          = "GRS"
  account_kind                      = "StorageV2"
  allow_nested_items_to_be_public   = false
  min_tls_version                   = "TLS1_2"
  infrastructure_encryption_enabled = true

  #   below tags are managed by CDC
  lifecycle {
    ignore_changes = [
      tags["business_steward"],
      tags["center"],
      tags["environment"],
      tags["escid"],
      tags["funding_source"],
      tags["pii_data"],
      tags["security_compliance"],
      tags["security_steward"],
      tags["support_group"],
      tags["system"],
      tags["technical_steward"],
      tags["zone"]
    ]
  }
}

resource "azurerm_storage_container" "metadata" {
  name                  = "metadata"
  storage_account_name  = azurerm_storage_account.storage.name
  container_access_type = "private"
}

resource "azurerm_role_assignment" "allow_api_read_write" {
  scope                = azurerm_storage_container.metadata.resource_manager_id
  role_definition_name = "Storage Blob Data Contributor"
  principal_id         = azurerm_linux_web_app.api.identity.0.principal_id
}
