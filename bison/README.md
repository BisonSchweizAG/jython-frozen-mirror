## How to get a green Eclipse workspace

- `git clone git@github.com:BisonSchweizAG/jython-frozen-mirror.git`
- `git checkout r2.5.3.bison`
- use a current eclipse installation without the Bison tools plugin: open an empty workspace
- add and set the default JDK to 1.8
- set the compiler compliance level to 1.8
- add the clone to the git perspective and import the project
- go to Window -> Preferences -> Java -> Code Style -> Formatter, and import `/bison/formatting/Jython-like.xml`
- set the `jdk7.command` property in `ant.properties`, e.g: `/usr/lib/jvm/jdk1.7.0_79/bin/java`
- make sure that the classpath of the `/bison/launch.configs/build developer.launch` and `/bison/launch.configs/setup.launch` launch configurations contains `/extlibs/ant.jar`
- run the `setup` launch configuration (in `/bison/launch.configs/`) <br/>(run again in case of an initial build failure)


## Build and test as a developer

- run the `build developer` launch configuration <br/>(the first run just after `clean` might fail - run again in this case)
- run the `javatests` launch configuration
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
* run the `build artifactory` launch configuration
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
