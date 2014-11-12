dcop-algorithms
===============

Framework for Local Algorithms used in Distributed Constraint Optimization Problems.


About
-------------------------------------
The package offers a modular framework for developing iterative approximate best-response algorithms for solving DCOPs and several algorithm implementations. [1]

- DSA-A (Distributed Stochastic Algorithm, variant A) [1,3]
- DSAN (Distributed Simulated Annealing) [2]
- JSFP-I (Joint Strategy Fictitious Play with Inertia) [4]
- WRM-I (Weighted Regret Monitoring with Inertia) [1,5]

Usage
-------------------------------------
Ensure Java 8 is used for the compilation and install SBT, as described in the README file of the "signal-collect" project.

The project has the following dependencies:
- signal-collect


Follow the compilation instructions in the "signal-collect" README.

After cloning the repository, go to the project folder and start SBT on the command line. Use the "assembly" command in SBT to generate a .jar file with dependencies.

To generate an Eclipse project, use the "eclipse" command on the SBT prompt and then follow the description in the "How to Develop in Eclipse" section of the "signal-collect" README.

Note
-----
Before submitting a job to a Torque or Slurm host, it is imperative to re-run "assembly" in SBT in order to have the last version of the .jar file run.


Bibliography:
-----------------------
- [1] Archie C. Chapman, Alex Rogers, Nicholas R. Jennings and David S. Leslie (2011). A unifying framework for iterative approximate best-response algorithms for distributed constraint optimization problems. The Knowledge Engineering Review, 26, pp 411-444. doi:10.1017/S0269888911000178.
- [2] Arshad, Silaghi, 2003. "Distributed Simulated Annealing and comparison to DSA", In Proceedings of the 4th International Workshop on Distributed Constraint Reasoning, Acapulco, Mexico
- [3] Zhang, Wang, Wittenburg, 2002. "Distributed stochastic search for constraint satisfaction and optimization: Parallelism, phase transitions and performance", In Proceedings  of AAAI-02 Workshop on Probabilistic Approaches in Search, 2002, pp. 53–59
- [4] Marden, Jason R., Gürdal Arslan, and Jeff S. Shamma. "Joint strategy fictitious play with inertia for potential games." Automatic Control, IEEE Transactions on 54.2 (2009): 208-220.
- [5] Arslan, G., Marden, J. R. & Shamma, J. S. 2007. "Autonomous vehicle-target assignment: a game theoretical formulation". ASME Journal of Dynamic Systems, Measurement and Control 129, 584–596.
