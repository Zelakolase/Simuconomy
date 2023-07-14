# Simuconomy
Simuconomy is a simple economic model that uses genetic variation and the economic system as a fitness function to make the economy more productive over time. The code is highly readable and maintainable.<br>
<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Attribution-ShareAlike 4.0 International License</a>.

## Class tree
- Environment/
    - GlobalVariables.java : The global configuration of the economy (inflation target, starting population, etc..).
- Libraries/
    - SparkDB.java : in-memory table object for free-market offers. (Original Project)[https://github.com/NaDeSys/SparkDB].
    - Statistics.java : Generates one CSV line, containing Wealth Coefficient of variation, Mean Panic Coefficient, Mean Demand per agent, Mean Supply per agent, Number of agents alive, Mean Wealth per agent, Mean Base Inflator Sensitivity (gene), Mean Base Supply Capacity (gene), Mean Base Demand Capacity (gene), Inflation rate (%).
- Objects/
    - Agent.java : Agent Object, with multiple individual properties (including genes).
- Operations/
    - Calculation.java : Calculate revenue, inflator, and other agent properties.
    - Demand.java : Buy products from the market.
    - Filter.java : Filter dead (unfitting) agents, and filter based on old age once reaching maximum population.
    - InflationControl.java : Controls Inflation by distributing money on agents, or taxing their wealth.
    - Reproduction.java : Every two adjacent agents will reproduce and pass on their genes.
    - Supply.java : Put products on market, with appropriate price.
- App.java : Main Instance

## Genetic Traits
- Base Inflator Sensitivity : Represents the amount where Price Inflator changes, it is a gene that changes up to 1% postively or negatively every iteration.
- Base Supply Sensitivity : Represents the amount where actual Supply capacity changes. Non-modifiable.
- Base Demand Sensitivity : Represents the amount where actual Demand capacity changes. Non-modifiable.

## Economic Stages
1. Agents put their supply in market.
2. Agents demand products from market.
3. Agents calculate their earnings, among other things.
4. The system filters dead agents (totally unfitted), and old age agents as the current population reaches the maximum population.
5. Remaining agents reproduce (after the fifth iteration), from 2 to 3 children for every agent pair (1 per iteration) that does not have panic coefficient lower than -5 or higher than 5, and never reproduce again. 35% of each agent's wealth is passed to the child, with the genetic traits (average mean), and added up-to-25% variability under normal Global Variables.
6. Export statistics and activate inflation control after the tenth iteration.

## Assumptions
1. There are no monopolies formed.
2. There is no government (and taxes, etc..).
3. Any agent can take up to 10,000 in debt (before dying of unfitness).
4. Evert agent tries to decrease production, and increase demand if the economic situation is good.
5. All agents produce and demand (buy and sell).

## Expected results
It is expected to see Average Supply divided by Average Demand to progress upwards. On default settings, the agent performance (AvgSupplyPerAgent/AvgDemandPerAgent) will increase from 0.8-1.0 to 2.0-3.0. Note that 2.0 means that the agents produces twice products than he consumes.

## Panic Coefficient
The panic coefficient controls how the agent corrects inflator coefficient (NOT THE GENE!), the supply capacity, and the demand capacity in the times of economic instability.<br><br>
Panic >= 0 will reduce demand, or increase production<br>
Panic < 0 will increase demand, or decrease production<br><br>
- Panic is increased if:
   1. demandCapacity is not fulfilled in 'Demand' stage
   2. wealth has reached zero or below zero
   3. all products are sold
   4. productionCapacity falls below zero
- Panic is decreased if:
   1. demandCapacity is fulfilled in 'Demand' stage
   2. not all products are sold
   3. demandCapacity falls below zero
   4. wealth is sufficient for demand