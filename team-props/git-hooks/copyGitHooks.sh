#!/bin/sh

PROGNAME=$(basename $0)
# use $LINENO as call-site while passing the error-msg
error_exit()
{
	echo "${PROGNAME}: ${1:-"Unknown Error"}" 1>&2
	exit 1
}

# $1 = source_file $2 destination_file $3 prefix_name
copy_hook_if_modified(){

  if [[ ! -f "$1" ]] && [[ ! -f "$2" ]];then
   echo "$3: no hook found"
   error_exit "$3: $LINENO: cannot process without a hook"
  fi

  if [[ -f "$1" && ! -f "$2" ]] || [[ `diff $1 $2` ]]; then
  echo "$3: hook getting configured..."
  cp "$1" "$2"
  chmod -R +x "$2"
  elif [[ -f "$2" ]]; then
  echo "$3: hook already configured"
  fi
}

#main(){

if [[ ! $# -gt 0 ]]; then
error_exit "$LINENO : please pass the base dir of project"
fi

if [[ ! -d "$1" ]]; then
error_exit "$LINENO : must pass a valid directory"
fi

if [[ ! -d "$1/.git" ]]; then
error_exit "$LINENO : not a valid git repository"
fi

#copy pre-commit hook from team-props to .git
#if [[ -f "$1/team-props/git-hooks/pre-commit.sh" && ! -f "$1/.git/hooks/pre-commit" ]]; then
#echo "copying pre-commit hook.."
#cp "$1/team-props/git-hooks/pre-commit.sh" "$1/.git/hooks/pre-commit"
#chmod -R +x "$1/.git/hooks/"
#elif [[ -f "$1/.git/hooks/pre-commit" ]]; then
#echo "pre-commit hook already configured"
#else
#echo "no pre-commit hook found"
#error_exit "$LINENO: cannot process without a pre-commit hook"
#fi
copy_hook_if_modified "$1/team-props/git-hooks/pre-commit.sh" "$1/.git/hooks/pre-commit" "pre-commit"

#copy pre-push hook from team-props to .git
#if [[ -f "$1/team-props/git-hooks/pre-push.sh" && ! -f "$1/.git/hooks/pre-push" ]]; then
#echo "copying pre-commit hook.."
#cp "$1/team-props/git-hooks/pre-push.sh" "$1/.git/hooks/pre-push"
#chmod -R +x "$1/.git/hooks/"
#elif [[ -f "$1/.git/hooks/pre-push" ]]; then
#echo "pre-push hook already configured"
#else
#echo "no pre-commit hook found"
#error_exit "$LINENO: cannot process without a pre-push hook"
#fi
copy_hook_if_modified "$1/team-props/git-hooks/pre-push.sh" "$1/.git/hooks/pre-push" "pre-push"

copy_hook_if_modified "$1/team-props/lint-ing/lint.xml" "$1/lint.xml" "lint.xml"

copy_hook_if_modified "$1/team-props/.editorconfig" "$1/.editorconfig" ".editorconfig"
#}
