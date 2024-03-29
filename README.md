# graalf

A testbed project using CRAL library for interacting with Alfresco that compiles down to native code using GraalVM.

### Build
Get the sources:
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
and copy the executable binary (target/uberjar/anvedi) somewhere in your path.

Fast startup!
```console
$ time ./anvedi --help
NAME:
 anvedi - A testbed project using CRAL library for interacting with Alfresco. Compatible with GraalVM.

USAGE:
 anvedi [global-options] command [command options] [arguments...]

VERSION:
 0.1.0-SNAPSHOT

COMMANDS:
   ticket               get a ticket
   children             list children

GLOBAL OPTIONS:
   -?, --help


real    0m0.012s
user    0m0.008s
sys     0m0.004s
```
Compared with standard JVM:
```console
$ time ./anvedi ticket -u admin -p admin
Authentication successful.

real    0m0.079s
user    0m0.000s
sys     0m0.014s
$ time java -jar anvedi-0.1.0-SNAPSHOT-standalone.jar ticket -u admin -p admin
Authentication successful.

real    0m1.174s
user    0m3.317s
sys     0m0.088s
```
## License
Copyright (c) 2023-2024 Saidone

Distributed under the GNU General Public License v3.0
