# Programming Standards – Tier List Maker
**Team: Runtime Terrors**

This document defines programming, version control, and quality standards for our project.

---

## 1. Naming Conventions

### General
- Names should be self-explanatory where possible

### Packages
- Lowercase

### Classes
- **Classes:** PascalCase
- **Interfaces:** PascalCase with prefix "I"
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
- Methods in classes should be order like:
```java
class Class {
    //varaibles
    
    //constructors
    
    //methods
    
}
```

### Comments
- **Don't:** Comment obvious code
- **Do:** Use JavaDoc for public logic layer functions
- **Do:** Explain why, not what (e.g., "Use X to do Y" vs "Does X")

---

## 4. Testing Requirements

### Unit Tests
- For all logic layer classes (80% coverage required)
- Location: `src/test/java`
- No tests required for Database, DSO, or Presentation layer classes

---

## 5. Version Control

### Naming Branches
- `issue-###-short-description` (swap ### with issue number)

### Merge Requests
- Clear description of what it does
- Issue reference (e.g., Closes #42)
- Test notes (how to verify the changes)

### Git Workflow

1. **Create feature branch:**
   - `git checkout main`
   - `git pull`
   - `git checkout -b issue-###-short-description`

2. **Work on your feature:**
   - Make changes
   - Format code
   - Run tests
   - Commit: `git commit -m "Descriptive description"`

3. **Before creating merge request:**
   - Pull main: `git pull origin main`
   - Resolve conflicts
   - Run tests
   - Push: `git push origin issue-###-short-description`

4. **Merge to main:**
   - Create Merge Request on GitLab
   - Get 1 approval
   - Merge on GitLab
   - Delete branch

**Commit Early, Commit Often**

---