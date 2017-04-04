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
- on Ubuntu, the result of the 'regrtests' is as follows:
```
     [exec] 3 tests skipped:
     [exec]     test_subprocess test_urllib2net test_urllibnet
     [exec] 7 tests failed:
     [exec]     test_float_jy test_import_jy test_marshal test_platform
     [exec]     test_socket test_sort test_traceback
     [exec] 7 fails unexpected:
     [exec]     test_float_jy test_import_jy test_marshal test_platform
     [exec]     test_socket test_sort test_traceback
     [exec] Result: 1
```


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


## (very) short summary of the changes to 2.5.3
* see also: `git diff r2.5.3`
* `PyJavaType.java`: No bean properties marker, e.g. for dynamic entity views
* `PyJavaType.java`: Suppress the `try/catch` method lookup for packages which clearly don't have methods `__get__`, `_doget` etc. declared
* `PyString.java`: StringFormatter automatically switches to unicode for java types (if range is outside ascii)
* `PyType.java`: Deviation for `protected final` java superclass methods
* `PySystemState.java`: Improvements for standalone recognition using protection domain URL (e.g. for WildFly virtual file system)
* `Lib/string.py`: Still added for backwards compatibility

 
## TODO (work in progress)
 - double check the protection domain URL when starting the WildFly container (on windows and on Linux)
 - find out how to correctly deploy the jython jars to artifactory (currently resolving of the binary jar works, but resolving of sources and javadoc is not working)
 - adjust the artifactory build target accordingly (if necessary)
 - cherry pick and test the fix for Issue 2487 (see http://darjus.blogspot.ch/2016/08/jython-release-delay-and-deadlocks.html)
 - document oracle.jar (`/bison/non-distributable`)
 - document informix.jar (`/bison/non-distributable`)
 - (if necessary at all:) eliminate the need to copy to the `checkout` dir manually for the full build
