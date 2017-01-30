## How to get a green Eclipse Neon workspace

- `git clone git@github.com:BisonSchweizAG/jython.git`
- `git checkout r2.5.3.bison`
- use a Neon installation without the Bison tools plugin: open an empty workspace
- add and set the default JDK to 1.7 (not higher)
- set the compiler compliance level to 1.6
- add the clone to the git perspective and import the project
- go to Window -> Preferences -> Java -> Code Style -> Formatter, and import `/bison/formatting/Jython-like.xml` 
- run the `setup` launch configuration (in `/bison/launch.configs/`) <br/>(run again in case of an initial build failure)


## Build and test as a developer

- run the `build developer` launch configuration <br/>(the first run just after `clean` might fail - run again in this case)
- run the `javatests` launch configuration
- run the `regrtests` launch confguration <br/>(on Mac: eagerly picks JDK 9 if installed and stops in `test_urllib2_localnet.py` with: "cannot access class sun.nio.ch.ServerSocketAdaptor (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @b1712f3")


## Build a deployable version 
* run the `build artifactory` launch configuration
* deploy `/dist/artifactory/jython*.jar` to `/ext-libs-local-modified/org/python/jython`
  * check the Deploy as Maven Artifact checkbox
* deploy `/dist/artifactory/jython*-sources.jar` to `/ext-libs-local-modified/org/python/jython`
  * check the Deploy as Maven Artifact checkbox
  * add the classifier `sources`
* deploy `/dist/artifactory/jython*-javadoc.jar` to `/ext-libs-local-modified/org/python/jython`
  * check the Deploy as Maven Artifact checkbox
  * add the classifier `javadoc`

 
## TODO (work in progress)
 - find out how to correctly deploy the jython jars to artifactory (currently resolving of the binary jar works, but resolving of sources and javadoc is not working)
 - adjust the artifactory build target accordingly (if necessary)
 - apply the bison specific changes / the cherry-picked fix for Issue 2487 (see http://darjus.blogspot.ch/2016/08/jython-release-delay-and-deadlocks.html)`
 - document oracle.jar (`/bison/non-distributable`)
 - document informix.jar (`/bison/non-distributable`)
 - (if necessary at all:) eliminate the need to copy to the `checkout` dir manually for the full build
