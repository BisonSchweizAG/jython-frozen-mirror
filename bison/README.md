## Get a green Eclipse neon workspace

- `git clone https://github.com/BisonSchweizAG/jython.git`
- `git checkout r2.5.3.bison`
- open an empty Eclipse neon workspace
- add and set the default JDK to 1.7 (not higher)
- set the compiler compliance level to 1.6
- add the clone to the git perspective and import the project
- run the `setup` launch configuration (in `/bison/launch.configs/`)


## Build and test as a developer

- run the `build developer` launch configuration
- run the `javatests` launch configuration
- run the `regrtests` launch confguration


## Build a full version 
 - still to be defined