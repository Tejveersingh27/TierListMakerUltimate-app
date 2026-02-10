# Version Control Standards – Tier List Maker

This document defines version control standards for our project.

---

### 1. Naming Branches
- `type/short-description` 

---

### 2. Merge Requests
- Clear description of what it does
- Issue reference (e.g., Closes #42)
- Test notes (how to verify the changes if applicable)

---

### 3. Git Workflow

1. **Create feature branch:**
   - `git checkout main`
   - `git pull`
   - `git checkout -b type/short-description`

2. **Work on your feature:**
   - Make changes
   - Format code
   - Run tests
   - Commit: `git commit -m "Descriptive description"`

3. **Before creating merge request:**
   - Check out local main `git checkout main`
   - Update local main `git pull`
   - Check out your branch `git checkout type/short-description`
   - Merge local main onto branch: `git merge main`
   - Resolve conflicts
   - Run tests
   - Push: `git push origin type/short-description`

4. **Merge to main:**
   - Create Merge Request on GitLab
   - Get 1 approval
   - Merge on GitLab
   - Delete branch

**Commit Early, Commit Often**

---

### 4. Branching Strategy Justification
Using type/short-description makes branch purposes clear. The type (e.g., feature, fix, docs, test, chore) indicates the kind of work, and the short description shows what is being changed. This improves communication, simplifies branch tracking, and is less burdensome than alternatives like issue-###-short-description.