# anvedi

A testbed project using CRAL library for interacting with Alfresco. Compatible with GraalVM.

### Build
get the sources:
```console
$ git clone https://github.com/saidone75/anvedi.git
```
produce an uberjar with leiningen:
```console
$ cd anvedi
$ lein uberjar
Compiling anvedi.utils
[...]
Created /home/saidone/anvedi/target/uberjar/anvedi-0.1.0-SNAPSHOT.jar
Created /home/saidone/anvedi/target/uberjar/anvedi-0.1.0-SNAPSHOT-standalone.jar
```
create a native binary (*need a GraalVM toolchain installed and configured*):
```console
$ lein native-image
========================================================================================================================
GraalVM Native Image: Generating '/home/saidone/anvedi/target/uberjar/anvedi' (executable)...
========================================================================================================================
[...]
========================================================================================================================
Finished generating '/home/saidone/anvedi/target/uberjar/anvedi' in 1m 55s.
Created native image /home/saidone/anvedi/target/uberjar/anvedi
```
and copy the executable binary (target/uberjar/anvedi) somewhere in your path