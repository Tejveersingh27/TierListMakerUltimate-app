# Programming Standards – Tier List Maker

This document defines programming, and quality standards for our project.

---

## 1. Naming Conventions

### General
- Names should be self-explanatory where possible

### Packages
- Lowercase, PascalCase

### Classes
- **Classes:** PascalCase
- **Interfaces:** PascalCase
- **Test Files:** {ClassName}Test

### Variables
- **Variables:** camelCase
- **Constants:** UPPER_CASE
- **Methods:** camelCase with a verb as prefix
- **Booleans:** camelCase with "is", "has", or "can" as prefix

---

## 2. Code Quality

**Follow SOLID principles where possible**

- Max 30 lines per method (UI code may be longer)
- Each class should be describable in 1 sentence

- **Do** Handle exceptions appropriately
- **Do** use `strings.xml` for all UI text:

- **Don't** use magic numbers or hard-coded strings

---

## 3. Code Formatting

### General
- Use Android Studio's default code formatter before committing
- Format code: `Ctrl+Alt+L`
- Line length: 120 characters max
- Always use braces for if/else statements
- Methods in classes should be ordered like:
```java
class Class {
    //variables
    
    //constructors
    
    //methods
    
}
```

### Comments
- **Don't:** Comment obvious code
- **Do:** Explain why, not what (e.g., "Use X to do Y" vs "Does X")

---

## 4. Testing Requirements

### Unit Tests
- For all logic layer classes (80% coverage required)
- Location: `app/src/test`

---
