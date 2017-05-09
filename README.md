# An Example with Play Framework and Cassandra (Quill)
This is an example REST API showing how to hook together Play Framework and Cassandra (using Quill)

See the [phantom branch](https://github.com/calvinlfer/Play-Framework-Cassandra-Example/tree/phantom) for a Phantom 
backed repository

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
