# An Example with Play Framework and Cassandra (Phantom)
This is an example REST API showing how to hook together Play Framework and Cassandra (using Phantom). 
Phantom has been isolated to the repository interpreter.

See the [quill branch](https://github.com/calvinlfer/Play-Framework-Cassandra-Example/tree/quill) for a Quill backed repository

The phantom branch also has pagination which is not implemented in the Quill branch.

This project was created using [giter8](http://www.foundweekends.org/giter8):

```sbt new playframework/play-scala-seed.g8```

## Cassandra setup (development)

### Keyspace
```cql
CREATE KEYSPACE persons WITH REPLICATION = {
	'class': 'SimpleStrategy',
	'replication_factor': 1
};
```

### Table
```cql
CREATE TABLE persons.person_info (
	id UUID,
	gender TEXT,
	student_id TEXT,
	first_name TEXT,
	last_name TEXT,
	PRIMARY KEY (id)
);
```