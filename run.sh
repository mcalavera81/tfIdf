#!/usr/bin/env bash

while getopts ":d:" opt; do
  case ${opt} in
    d )
      target=$OPTARG
      ;;
    : )
      echo "Invalid option: $OPTARG requires an argument" 1>&2
      ;;
  esac
done

docker run --rm -v "$PWD/$target":/app/input puzzle "$@"
