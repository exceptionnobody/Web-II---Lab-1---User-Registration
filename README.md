# wa2-g12-user-registration

Repository for the Lab 3 assignment of the Web Applications II course at Polytechnic University of Turin (academic year 2021-2022).

## Group 12 members:
| Student ID | Surname | Name |
| --- | --- | --- |
| s286154 | Ballario | Marco |
| s277873 | Galazzo | Francesco |
| s276086 | Tangredi | Giovanni |
| s292522 | Ubertini | Pietro |

## Usage

The Login service can be found in this repository on the branch ```lab-4```:
- ```git checkout lab-4```

In case you change the ```POSTGRES_PASSWORD``` value remember to change the ```spring.datasource.password``` field in ```applications.properties```

The prune expired data job is executed every 1 minute.
To change that modify in ```application.properties``` the fields:
1. ```job.execution.rate```
2. ```job.initial.delay```