# Init-o-matic

Initial PoC of Init-o-matic (ih-nit-OH-mat-ik).

Where the improbable becomes probable.  Sometimes, maybe.

## Overview

## Project Setup

To get started, clone this repo and in the project root folder execute the gradle wrapper with
default parameters: `./gradlew`.  This will perform a default lifecyle `clean build` for you.

Once built, there are two startup scripts that you can use:

* `./runDev.sh` - run in Development mode
* `./runDep.sh` - run in Deployment mode

Additionally, you should be able to import the project easily into your favorite IDE (this claim
has only been tested with IntelliJ).  The default run configuration should work out of the box and
default to Development mode.

## Architecture

### Plugin Modes

It is important to understand the different modes:

Development mode is intended for most local development use cases.  Specifically, the plugin
framework pf4j looks for plugins present in the base ./plugins path.  This mode expects that each
subfolder contains an un-packaged plugin.  Additionally, a single classloader is used to facilitate
debugging within an IDE.  This mode will make it much easier to make code changes in plugins and
have it available without requiring a full rebuild of the project.

Deployment mode is used when the application is deployed to a target environment.  In this mode
pf4j looks for a specific folder where available plugins are bundled as .zip packages (the default
folder is in the ./plugins/build/plugins).  Unlike Development mode, each plugin gets its own
classpath which should avoid library conflicts.

Warning: Development mode is activated by default which is not the default behavior of pf4j.

## Contributing

TODO

## TODOs

TODO?

### Plugins

* Metadata
  * identity
  * blueprint
  * infrastructure
* Git
  * init
  * commit
  * push
  * clone
* Github
* Gitlab
* Spring Initializr
* Template processor
* Infrastructure
  * Cloud Foundry
  * Kubernetes
  * Open Service Broker (Generic)
* CI/CD
  * Concourse CI
  * TeamCity
* Issues
* Wiki
* Kanban/Issue