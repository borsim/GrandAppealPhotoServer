### GrandAppealPhotoServer
## Software Product Engineering at the University of Bristol, project #30

## Hello everyone!
# I'm Miki, the team leader and this is my Git process guide!

We will list development tasks as well as errors and all other concerns logged as Issues;
however minor the thing just open an **Issue** for it! It's not a problem to have many and they are easy to track.
I recommend using git in the terminal, commands will be listed as in that environment.

We will always keep a working version as the "master" branch of the repository. 
You should **NEVER EVER work on the master branch, nor write to it**. I don't know how to set up the right guards
so there will be no software message popping up. Just please keep it in mind :) .

Before you start working on an issue, do a `` git pull `` to update your local environment to the current version.
This is not super-crucial but can prevent problems down the line so I do recommend doing it.

Select an issue and before you start working, do `` git branch yourname-whatyourefixing `` to create a new branch
to work on and make it easily identifiable later.

Do your work and make regular commits with `` git commit -am "This message describes what I modified" `` to save your progress.
This helps you go back to a point if something unfixable comes up and you need to redo everything. 

Once your think the code is working, understandable and stable, push it up on the online repository (this!).
Use `` git push origin yourname-whatyourefixing `` to push your current branch up to the "origin", which is this thing on GitHub.
Then open up your web browser and go to **Pull requests** and open a new pull request.
Write a brief description of what the pull request does. 
If you write "Closes #176" then once the pull request is accepted and merged the relevant Issue will automatically be closed!
If your pull request resolves multiple Issues, you have to put each Closes#123 in a separate line.

Now you wait.

Wait for at least one team member to check your work. GitHub allows you to easily see the modifications done to the existing code 
at the Pull requests's Review tab.
If you notice something changed, whether it's an error or just could be done in a more reusable and understandable manner, review 
and put a comment at that specific line! This is code review so please abandon all notions of politeness and
try to be as clear as possible! Being brief and clear is the most important factor here.
If the code is a-ok and beautiful, give it an Approved review.
Once it's been approved, the branch can then be merged. This will be a button on the Pull Request web page.

This branch has now served its purpose. On your local computer you should do `` git branch -d yourname-whatyourefixing `` to get 
get rid of that already merged branch to reduce clutter.

Some other useful commands:
If you mess up some changes and want to just jump back to your previous commit to check something, do
`` git stash ``    - this will store all changes since the last commit and put you back to it. If you stash again, it will jump 
back another commit.
`` git stash list `` will list all the stashes you have
`` git stash apply 5 `` will apply the stashed changes at index 5 (take note that this is 0-indexed. If you don't give it a number, it will apply #0.

To get the repo on your local computer, do:
`` git clone https://github.com/borsim/GrandAppealPhotoServer.git ``
(also usable if everything goes FUBAR and you just want the working version again)

GitHub supports Markdown formatting so if you want your reviews to be **fancy**, try these!
Put words between a pair of stars to make them *italic*
Put words between a pair of two stars to make them **bold**
Put words between a pair of tildes to make them ~~strikethrough~~
Put words between a pair of backticks to make them inline `code`
Put a code block between a pair of three backticks to make it an actual code block
``` class Gromit (int episode) {
  private boolean watched = false;
  //... other code
} 
```
Put hashtags and a space in front of them (at the beginning of a line) to make them increasingly larger 
# Headers
## Headers
### Headers

