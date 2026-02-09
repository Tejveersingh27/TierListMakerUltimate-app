# ARCHITECTURE
This document provides an overview of the project's architecture

---

## 1. Diagram
![Image of Our Architecture Diagram](images/Architecture.png)


---

## 2. Layers

### Presentation Layer
**TierListView**
- Class to let users interact with the tier list (starting screen of the app)

**TierEditorDialog**
- Class to edit the appearance of the tiers

**ImageImportDialog**
- Class to allow users to import their images as TierItems

**ShareTierListDialog**
- Class to allow users to export the tier list as an image

--- 

### Logic Layer
**TierListManager**
- Class to modify and create TierLists

**TierManager**
- Class to modify tiers in TierLists (the structure of the TierList)

**ItemPlacementManager**
- Class to modify the positions of items in TierLists

**ImageIOManager**
- Class to manage image I/O for taking screenshots and saving TierItem images

--- 

### Domain-Specific Objects
**TierList**
- Class for TierLists

**Tier**
- Class for Tiers

**TierItem**
- Class for TierItems

--- 

### Persistence Layer
**TierListPersistence**
- Interface for TierLists in the database

**TierPersistence**
- Interface for Tiers in the database

**TierItemPersistence**
- Interface for TierItems in the database

---

## 3. Component Interactions
### Dependency Flow Overview
   `Presentation   →   Logic   →   Persistence   →   Database`

### Example: User Moves a TierItem

1. **User** → Drags item from "Tier S" to "Tier A"

2. **Presentation (TierListView)** 
   - Listener detects drop on Tier A
   - Gets tier ID from item
   - Calls: `itemPlacementManager.moveItemToTier(itemId, targetTierId)`

4. **Logic (ItemPlacementManager)**
   - Validates: Does item exist? Does target tier exist?
   - Calls: `itemStorage.updateItem(updatedItem)`

5. **Persistence (TierItemPersistence)**
   - Queries: `UPDATE tier_items SET tier_id = ? WHERE id = ?`
   - Returns: Updated TierItem object

6. **Presentation (TierListView)**
   - Call refreshUI() to reload TierList 