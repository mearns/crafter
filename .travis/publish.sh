#!/bin/bash

### Config

# Our access token won't work for forks anyway, but we may as well bail out early.
YOUR_REPO="mearns/crafter"

#The user name and email that will show up one the commit to your gh-pages branch.
#Alternatively, use $GIT_NAME and $GIT_EMAIL to extract the variables from the secure
# block in the .travis.yml config file.
YOUR_EMAIL="travis@travis-ci.org"
YOUR_NAME="travis-ci build script"


#################################

if [ "$TRAVIS_REPO_SLUG" == "$YOUR_REPO" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing javadoc...\n"

  echo -e "...Copying javadocs...\n"
  cp -R build/docs/javadoc $HOME/javadoc-latest

  echo -e "...Configuring git client as \"$YOUR_NAME\" <$YOUR_EMAIL>...\n"
  cd $HOME
  git config --global user.email "$YOUR_EMAIL"
  git config --global user.name "$YOUR_NAME"

  echo -e "...Cloning gh-pages...\n"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/$YOUR_REPO gh-pages > /dev/null

  echo -e "...Deleting old javadocs...\n"
  cd gh-pages
  git rm -rf ./javadoc

  echo -e "...Adding latest javadocs...\n"
  cp -Rf $HOME/javadoc-latest ./javadoc
  git add -f .
  git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"

  echo -e "...Pushing to gh-pages...\n"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
  
fi

