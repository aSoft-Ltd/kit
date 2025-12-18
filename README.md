# Getting Started with Kit and Git at aSoft
A practical guide for setting up your development environment and working with Kit, our Git workflow tool.

## What is Kit?

Kit is a command-line tool that simplifies Git operations across multiple repositories and submodules. Instead of running git commands in each submodule individually, Kit lets you manage everything from the parent repository.

## Prerequisites

- Git installed on your system
- SSH key configured with GitHub
- Basic terminal knowledge

## Part 1: Installing Kit

### Step 1: Download Kit

Go to the [Kit releases page](https://github.com/aSoft-Ltd/kit/releases/tag/0.0.1) and download the appropriate version for your system.

For Linux, download `kit-linux`.

### Step 2: Install Kit (Linux/Manjaro)

After downloading, open your terminal and make the file executable:

```bash
chmod +x ~/Downloads/kit-linux
```

Move it to your system binaries and rename it:

```bash
sudo mv ~/Downloads/kit-linux /usr/local/bin/kit
```

Verify the installation:

```bash
which kit
kit help
```

You should see the Kit help menu with available commands.

## Part 2: Setting Up SSH for GitHub (Important!)

**Common Issue**: Using HTTPS URLs for cloning will cause problems with recursive submodules. You need to force Git to use SSH instead.

Configure Git to always use SSH for GitHub:

```bash
git config --global url."git@github.com:".insteadOf "https://github.com/"
```

This ensures all GitHub operations use your SSH key, even when a repository references HTTPS URLs.

## Part 3: Cloning the Home Repository

### Clean Start (if needed)

If you had previous failed attempts, clean up first:

```bash
cd ~
rm -rf asoft
mkdir asoft
cd asoft
```

### Clone with Submodules

Now clone the home repository with all its submodules recursively:

```bash
git clone --recursive git@github.com:aSoft-Ltd/home.git
cd home
```

The `--recursive` flag ensures all nested submodules are cloned too. This might take a few minutes depending on the number of submodules.

## Part 4: Working with Kit

Now that everything is set up, here's how to use Kit for common operations.

### Check Repository Status

See the status of all submodules at once:

```bash
kit status
```

### Creating Your Development Branch

Create your personal development branch across all submodules:

```bash
git submodule foreach --recursive "git switch -C dev-yourname || true"
```

Replace `dev-yourname` with your actual branch name (e.g., `dev-neicore`).

The `|| true` ensures the command continues even if a branch already exists in some submodules.

**Important**: At aSoft, you work directly on your developer branch (`dev-yourname`). Only create feature branches if you're working on something new while your current changes are pending review.

### Fetching Updates

Fetch changes from the remote repository:

```bash
kit fetch origin main
```

Or fetch across all submodules:

```bash
git submodule foreach --recursive "git fetch origin"
```

### Merging Changes

Merge changes from origin/main into your current branch:

```bash
kit merge origin main
```

### Adding and Committing Changes

The quick way (recommended):

```bash
kit ac "your commit message"
```

This adds all changes and commits them with your message in one command.

**Example**: You're working on academia-client and majestic for one feature. Once done:

```bash
cd ~/asoft/home
kit ac "implemented user authentication flow"
```

Kit will automatically add and commit changes across all repositories you've modified.

### Pushing Changes

Push your changes to your development branch:

```bash
kit push origin dev-yourname
```

Example:

```bash
kit push origin dev-neicore
```

Kit automatically pushes changes only in the repositories where you've made commits.

## Part 5: The aSoft Pull Request Workflow

### Step 1: Make Your Changes

Work on your features across multiple repositories as needed (e.g., academia-client, majestic, overwatch-client).

### Step 2: Commit Your Changes

Once you're done with a feature or logical unit of work:

```bash
cd ~/asoft/home
kit ac "descriptive message about what you changed"
```

**Example commits:**

- `kit ac "implemented user authentication flow"`
- `kit ac "fixed navigation bug in dashboard"`
- `kit ac "added validation to form inputs"`

### Step 3: Push to GitHub

Push your changes to your development branch:

```bash
kit push origin dev-yourname
```

Example:

```bash
kit push origin dev-neicore
```

### Step 4: Create Pull Request(s)

Go to GitHub and create pull requests for each repository you modified. Set the base branch to `main` and compare with your branch (`dev-yourname`).

### Step 5: Notify Reviewers on WhatsApp

Post in the team WhatsApp group to inform reviewers. Common messages:

**When you push changes:**

```
⚙️ Changes pushed
```

**When you create/update a PR:**

```
[PR link]
@ReviewerName
```

**When changes are requested:**

```
♻️ Changes Requested
```

### Step 6: Address Review Feedback

If reviewers request changes:

1. Make the requested changes in your code
2. Commit: `kit ac "addressed review comments"`
3. Push: `kit push origin dev-yourname`
4. Notify in WhatsApp: `⚙️ changes pushed`

### Working on Multiple Features

If you need to work on something new while your current changes are pending review, create a feature branch:

```bash
git checkout -b feature/new-feature-name
```

Once your main branch changes are merged, switch back and continue working.

## Part 6: Common Git Operations

### Check Which Branch You're On

In all submodules:

```bash
git submodule foreach "git branch"
```

### Switch Branches

Switch to an existing branch across all submodules:

```bash
git submodule foreach --recursive "git switch dev-yourname || true"
```

### Delete a Local Branch

If you need to delete a branch:

```bash
git branch -D branch-name
```

### View Commit History

```bash
git log
```

## Part 7: Available Kit Commands

Here's a quick reference of Kit commands:

- `kit status` - Show status of all repositories
- `kit ac "message"` - Add and commit changes with message (most used!)
- `kit fetch` - Fetch updates from remote
- `kit merge` - Merge changes from remote branch
- `kit push origin dev-yourname` - Push changes to your branch
- `kit help` - Show help menu

## Troubleshooting

### Issue: Submodules not cloning properly

**Solution**: Make sure you've configured SSH as shown in Part 2, then try cloning again with the `--recursive` flag.

### Issue: "Permission denied" when running kit

**Solution**: Make sure the file is executable:

```bash
sudo chmod +x /usr/local/bin/kit
```

### Issue: Command not found after installation

**Solution**: Check if the file is in your PATH:

```bash
which kit
echo $PATH
```

## Quick Reference Cheat Sheet

```bash
# Setup (one time)
git config --global url."git@github.com:".insteadOf "https://github.com/"
git clone --recursive git@github.com:aSoft-Ltd/home.git

# Daily workflow
cd ~/asoft/home
kit status                           # Check what's changed
kit fetch origin main                # Get latest updates
kit merge origin main                # Merge updates into your branch

# ... make your changes across repos ...

kit ac "implemented feature X"       # Add and commit all changes
kit push origin dev-yourname         # Push to your branch

# Create PR on GitHub, then post in WhatsApp:
# "⚙️ Changes pushed"

# Branch management
git submodule foreach --recursive "git switch -C dev-yourname || true"
git submodule foreach "git branch"   # See all branches
```

## Real Workflow Example

Let's say you're implementing a new authentication feature that touches both academia-client and majestic:

```bash
# 1. Start your day - get latest changes
cd ~/asoft/home
kit fetch origin main
kit merge origin main

# 2. Make sure you're on your dev branch
git submodule foreach --recursive "git switch dev-neicore || true"

# 3. Work on your feature
# Edit files in academia-client/...
# Edit files in majestic/...

# 4. Check what you changed
kit status

# 5. Commit your work
kit ac "implemented user authentication flow"

# 6. Push to GitHub
kit push origin dev-neicore

# 7. Create PRs on GitHub for academia-client and majestic

# 8. Post in WhatsApp
# "⚙️ Changes pushed"
# Tag your reviewer

# 9. If changes requested, make fixes and repeat steps 5-8
kit ac "addressed review comments"
kit push origin dev-neicore
# Post: "⚙️ changes pushed"
```

## Tips for New Developers

1. **Always work on your dev branch** (`dev-yourname`) - only create feature branches if your current work is pending review
2. **Use `kit ac "message"`** for quick commits - it's the most common command
3. **Push with your branch name**: `kit push origin dev-yourname`
4. **Check status frequently**: `kit status` shows what you've changed
5. **Communicate on WhatsApp**: Use "⚙️ Changes pushed" when you push, tag reviewers
6. **Fetch and merge regularly**: Stay up to date with main to avoid conflicts
7. **Kit is smart**: It only pushes repos where you've made changes
8. **Commit messages matter**: Be descriptive about what you changed
9. **One feature, one commit cycle**: Complete logical units of work before committing
10. **When in doubt, ask**: Better to ask in the group than to mess up branches

## Need Help?
- Ask Isaka
- Ask in the team in WhatsApp chat