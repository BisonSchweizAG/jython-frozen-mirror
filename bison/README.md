## How to get a green Eclipse workspace

- `git clone git@github.com:BisonSchweizAG/jython-frozen-mirror.git`
- `git checkout r2.5.3.bison`
- use a current eclipse installation without the Bison tools plugin: open an empty workspace
- add and set the default JDK to 1.7 (higher versions are OK for running tests)
- set the compiler compliance level to 1.7
- add the clone to the git perspective and import the project
- go to Window -> Preferences -> Java -> Code Style -> Formatter, and import `/bison/formatting/Jython-like.xml`
- set the `jdk7.command` property in `ant.properties`, e.g: `/usr/lib/jvm/jdk1.7.0_79/bin/java` (is needed if run from Eclipse, but a double definition if run from the command line with jdk 1.7)
- it is OK if the ant launch configurations are executed with 1.8 or higher (Eclipse requires >= 1.8), but please make sure to build again on the command line with jdk 1.7 before deploying
- run the `setup` launch configuration (in `/bison/launch.configs/`) (run again in case of an initial build failure)


## Build and test as a developer
- run the `clean` launch configuration
- run the `build developer` launch configuration (the first run just after `clean` might fail - run again in this case)
- run the `javatests` launch configuration (make sure to run it with jdk 11)
- run the `regrtests` launch configuration <br/>(on Mac: eagerly picks the latest installed JDK -> tweak `dist/bin/jython` to use jdk 11 and `--illegal-access=warn`, around line 263)
- on Ubuntu, the result of the 'regrtests' is as follows:
```
     [exec] 323 tests OK.
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
* check/update the `jython.version`(s) in `ant.properties`
* if necessary, add a new tag like `git tag -a v2.5.3.5 -m "Jython version 2.5.3.5"`
* if necessary, push the new tag like `git push origin v2.5.3.5`
* setup your shell to use ant 1.9.16 and jdk 7 to run the builds from the command line (jarjar-0.7 does **not** work with jdk >= 8)
  * `cd jython` (to the directory containing `build.xml`)
  * `ant -version` should print `Apache Ant(TM) version 1.9.16 compiled on July 10 2021`
  * `java -version` should print `Java(TM) SE Runtime Environment (build 1.7.0_nn-bnn)` where `nn` is any number
  * `ant -buildfile ./build.xml clean`   
  * `ant -buildfile ./build.xml developer-build`
  * leave out the tests if executed above (e.g. from Eclipse)
  * `ant -buildfile ./build.xml artifactory`
* deploy `/dist/artifactory/jython*-sources.jar` to `/ext-libs-local-modified/org/python/jython`
  * check the Deploy as Maven Artifact checkbox
  * Group ID: `org.python`
  * Artifact ID: `jython`
  * Version: the actual version (from ant.properties)
  * Classifier: `sources`
  * Type: `jar`
* deploy `/dist/artifactory/jython*-javadoc.jar` to `/ext-libs-local-modified/org/python/jython`
  * check the Deploy as Maven Artifact checkbox
  * Group ID: `org.python`
  * Artifact ID: `jython`
  * Version: the actual version (from ant.properties)
  * Classifier: `javadoc`
  * Type: `jar`
* deploy `/dist/artifactory/jython*.jar` to `/ext-libs-local-modified/org/python/jython`
  * check the Deploy as Maven Artifact checkbox
  * do not be afraid if completely wrong IDs appear (`jline`) - simply overwrite them like indicated below
  * Group ID: `org.python`
  * Artifact ID: `jython`
  * Version: the actual version (from ant.properties)
  * leave Classifier empty 
  * Type: `jar`
* deploy the `.pom` file with the correct name/version
  * prepare it in `jython/bison` first
  * Group ID: `org.python`
  * Artifact ID: `jython`
  * Version: the actual version (from ant.properties)
  * leave Classifier empty 
  * Type: `pom`

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
