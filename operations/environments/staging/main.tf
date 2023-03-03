terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.45.0"
    }
    azuread = {
      source  = "hashicorp/azuread"
      version = "2.36.0"
    }
  }

  # Use a remote Terraform state in Azure Storage
  backend "azurerm" {
    resource_group_name  = "cdcti-terraform"
    storage_account_name = "cdctiterraform"
    container_name       = "tfstate"
    key                  = "staging.terraform.tfstate"
  }
}

# Configure the Microsoft Azure Provider
provider "azurerm" {
  features {}
}

provider "azuread" {

}

module "template" {
  source = "../../template/"

  environment = "staging"
}
