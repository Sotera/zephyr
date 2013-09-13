Zephyr Storm
====================

Unlike Zephyr-MapReduce, it's not entirely feasible to describe a topology via configuration file.  Or, rather, it is, but only the very simplest Topology might be defined.

Instead of trying to pigeon-hole everyone into the same, hyper simplistic topology, we instead took a lighter approach; define the sorts of Bolts we might expect to be used in a Zephyr-Storm topology and identify the streams they will connect to each other upon.

Other than that, it's up to you to define your topology.  Check our sample-project/sample-project-storm for an example Topology for how it should be used - and the README.md in that folder to describe its behavior. 