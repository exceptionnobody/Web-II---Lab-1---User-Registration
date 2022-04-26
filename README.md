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
2. Create and run Docker container:
```docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 54320:5432 -v Path\To\Project\Volume\Folder:/var/lib/postgres/data postgres```
