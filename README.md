# anvedi

A testbed project using CRAL library for interacting with Alfresco and compiles down to native code using GraalVM.

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
$ time java -jar target/uberjar/anvedi-0.1.0-SNAPSHOT-standalone.jar ticket -u admin -p admin
TICKET_073f37c8ff6560a97384b2211485ce93115abebf

real    0m1.104s
user    0m3.109s
sys     0m0.073s
$ time target/uberjar/anvedi ticket -u admin -p admin
TICKET_073f37c8ff6560a97384b2211485ce93115abebf

real    0m0.090s
user    0m0.005s
sys     0m0.010s
```
## License
Copyright (c) 2023 Saidone

Distributed under the GNU General Public License v3.0