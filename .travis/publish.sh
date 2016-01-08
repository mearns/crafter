#!/bin/bash
##
## This script is called by travis CS after a successful build (as specified in the the "after_success" section
## of the main .travis.yml config file). It is used to publish stuff to the github pages branch for this project.
##

### Config

# Our access token won't work for forks anyway, but we may as well bail out early if it's submitting from the wrong repo.
YOUR_REPO="mearns/crafter"

#The user name and email that will show up one the commit to your gh-pages branch.
#Alternatively, use $GIT_NAME and $GIT_EMAIL to extract the variables from the secure
# block in the .travis.yml config file.
#YOUR_EMAIL="travis@travis-ci.org"
#YOUR_NAME="travis-ci build script"
YOUR_EMAIL=$GIT_EMAIL
YOUR_NAME=$GIT_NAME


#################################

if [ "$TRAVIS_REPO_SLUG" == "$YOUR_REPO" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing files to gh-pages...\n"

  echo -e "...Copying files...\n"
  cp -R build/docs/javadoc $HOME/javadoc-latest
  cp -R build/reports/tests $HOME/test-report-latest
  cp -R build/reports/jacoco/test/html $HOME/jacoco-report-latest
  cp -R build/reports/jacoco/test/jacocoTestReport.xml $HOME/jacoco-report-latest.xml
  cp -R build/reports/findbugs $HOME/findbugs-report-latest

  echo -e "...Configuring git client as \"$YOUR_NAME\" <$YOUR_EMAIL>...\n"
  cd $HOME
  git config --global user.email "$YOUR_EMAIL"
  git config --global user.name "$YOUR_NAME"

  echo -e "...Cloning gh-pages...\n"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/$YOUR_REPO gh-pages > /dev/null

  echo -e "...Deleting old files...\n"
  cd gh-pages
  git rm -rf ./javadoc
  git rm -rf ./test-report
  git rm -rf ./jacoco-report
  git rm -rf ./findbugs-report

  echo -e "...Adding latest files...\n"
  cp -Rf $HOME/javadoc-latest ./javadoc
  cp -Rf $HOME/test-report-latest ./test-report
  cp -Rf $HOME/jacoco-report-latest ./jacoco-report
  cp -f  $HOME/jacoco-report-latest.xml ./jacoco-report.xml
  cp -Rf $HOME/findbugs-report-latest ./findbugs-report
  git add -f .
  git commit -m "Lastest files from successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"

  echo -e "...Pushing to gh-pages...\n"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published files to gh-pages.\n"
  
fi

