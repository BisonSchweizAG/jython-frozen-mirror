## How to get a green Eclipse Neon workspace

- `git clone https://github.com/BisonSchweizAG/jython.git`
- `git checkout r2.5.3.bison`
- open an empty Eclipse neon workspace
- add and set the default JDK to 1.7 (not higher)
- set the compiler compliance level to 1.6
- add the clone to the git perspective and import the project
- run the `setup` launch configuration (in `/bison/launch.configs/`) <br/>(run again in case of an initial build failure)


## Build and test as a developer

- run the `build developer` launch configuration <br/>(the first run just after `clean` might fail - run again in this case)
- run the `javatests` launch configuration <br/>(`ImportTests#testImportFromJava` currently fails with "No module named testpkg.submodule")
- run the `regrtests` launch confguration <br/>(on Mac: eagerly picks JDK 9 if installed and stops in `test_urllib2_localnet.py` with: "cannot access class sun.nio.ch.ServerSocketAdaptor (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @b1712f3")


## Build a deployable version 
 - run the `build artifactory` launch configuration
 - get the `.jar` files from `/dist/artifactory/` 

 
## TODO (work in progress)
 - find out the correct names for the artifactory jars (and define them in `ant.properties`)
 - apply the bison specific changes to `r2.5.3.bison`
 - document oracle.jar (`/bison/non-distributable`)
 - document informix.jar (`/bison/non-distributable`)
 - (if necessary at all:) eliminate the need to copy to the `checkout` dir manually for the full build
