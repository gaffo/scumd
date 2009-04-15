#! /bin/bash

cd /tmp/reposrc/rails

rm -rf /tmp/repos/proj-2/rails.git
mkdir -p /tmp/repos/proj-2/rails.git
pushd /tmp/repos/proj-2/rails.git
git init --bare
popd

git branch -mv master m2
git checkout -b master db045dbbf60b53dbe013ef25554fd013baf88134
git branch -D m2

for commit in `git log --summary --reverse origin/master | grep -E "^commit" | awk '{print $2}'`
do
  echo $commit
  git cherry-pick $commit
  git push ssh master:master
  rm -rf .git/index.lock
done