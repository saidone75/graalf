# graalf

A testbed project using CRAL library for interacting with Alfresco that compiles down to native code using GraalVM.

### Build
Get the sources:
```console
$ git clone https://github.com/saidone75/graalf.git
```
produce an uberjar with leiningen:
```console
$ cd graalf
$ lein uberjar
Compiling graalf.utils
[...]
Created /home/saidone/graalf/target/uberjar/graalf-0.1.0-SNAPSHOT.jar
Created /home/saidone/graalf/target/uberjar/graalf-0.1.0-SNAPSHOT-standalone.jar
```
create a native binary (*need a GraalVM toolchain installed and configured*):
```console
$ lein native-image
========================================================================================================================
GraalVM Native Image: Generating '/home/saidone/graalf/target/uberjar/graalf' (executable)...
========================================================================================================================
[...]
========================================================================================================================
Finished generating '/home/saidone/graalf/target/uberjar/graalf' in 1m 55s.
Created native image /home/saidone/graalf/target/uberjar/graalf
```
and copy the executable binary (target/uberjar/graalf) somewhere in your path.

Fast startup!
```console
$ time ./graalf --help
NAME:
 graalf - A testbed project using CRAL library for interacting with Alfresco. Compatible with GraalVM.

USAGE:
 graalf [global-options] command [command options] [arguments...]

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
$ time ./graalf ticket -u admin -p admin
Authentication successful.

real    0m0.079s
user    0m0.000s
sys     0m0.014s
$ time java -jar graalf-0.1.0-SNAPSHOT-standalone.jar ticket -u admin -p admin
Authentication successful.

real    0m1.174s
user    0m3.317s
sys     0m0.088s
```
## License
Copyright (c) 2023-2024 Saidone

Distributed under the GNU General Public License v3.0
