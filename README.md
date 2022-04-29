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

1. Pull the official postgres image:
```docker pull postgres```
2. Create and run the Docker container:
```docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 54320:5432 -v Path\To\Project\Volume\Folder:/var/lib/postgres/data postgres```
3. Start the server

To stop the Docker container:
```docker stop postgres```</br>
To delete the Docker container:
```docker rm postgres```


In case you change the ```POSTGRES_PASSWORD``` value remember to change the ```spring.datasource.password``` field in ```applications.properties```

The prune expired data job is executed every 1 minute.
To change that modify in ```application.properties``` the fields:
1. ```job.execution.rate```
2. ```job.initial.delay```