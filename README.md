# traceMixer

This is a help program for FETA 

Mixes traces of queries executed in isolation, to simulate a concurrent execution.

## Run traceMixer

In order to execute **traceMixer**, you must run the command:

`$ java -jar "traceMixer.jar" -[option]`

Were the options are:

`--engine` or `-e <engine_used_for_traces>`: for isolated queries' traces used as input, which are generated with either "anapsid" or "fedx" (by default "fedx")

`--order` or `-o <execution_order_simulation>`: for setting "serial" or "random" simulated execution, of the isolated queries' traces used as input

`--block` or `-b <continius_block_size_of_subqueries>`: for setting the max size of block of continius subqueries, from each isolated query's trace

`--delay` or `-d <delay_between_consequtive_subqueries>`:  for setting the max delay between two consequtive subqueries, of the produced mixed federated log

**Note**: this programm will mix all files with suffix ".txt", that are present in the same folder than the ".jar" file
