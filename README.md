# mars-exploration-1

## Story

Mars exploration (sprint 1)
Looks like humanity will have a bright future after all: the colonization of Mars has finally started. But it is no small effort. To avoid wasting billions of dollars worth of equipment in space, a lot of simulation exercises need to be done – here on Earth.

To fully flesh out the Mars rovers software, sample maps are needed to calibrate its functions. This is where you come into the picture. Your team is selected to be a part of this wonderful adventure. Your first task is to create an application that can generate randomized maps of Mars, based on some input requirements. These requirements can change quickly, so you need to build a robust software that can handle changing requirements in a flexible way.

In the first iteration, you need to generate four types of objects: mountains (`^`), pits (`#`), minerals (`*`), and pockets of water (`~`). The first two are patch-like terrain elements (covering a 2D continuous area on the map), the latter two are point-like resources (covering just one pixel on the map).

Define the requirements for the resulting map through a configuration object. Along with the file name (to save the results in) and the width of the square map, try to set the most important general parameters of the requested terrain elements and resources in a compact way:

- The most important parameters are the areas covered by the different terrain elements (for example, three mountain regions are required, with areas `10`, `20`, and `30`, and two pits with areas `5` and `15`).
- A given number of mineral resource points must be placed next to mountain regions (if there is enough room), while a given number of pockets of water must be placed next to pits.


The map generator needs to create a new random map that conforms to the configuration parameters every time it runs. Experiment with different shape forming and arrangement strategies to create maps as realistic as possible. Try to include these strategies in the configuration object as well.

## What are you going to learn?

- Practice SOLID principles.
- Write text into files.
- Structure complex algorithms into multiple steps and classes.
- Practice other design principles such as SLAP and YAGNI.

## Tasks

1. Map configuration\
    Create a custom map configuration object that sets all the important features for the map.
   - There is a configuration object that contains the file name, map width, and the symbols and numbers of different terrain elements and resources, as well as the areas of the 2D-regions.
   - The generated map is different every time it runs, in accordance with the provided parameters.


2. Shape generation and placement\
   Create 2D objects (mountains and pits) one-by-one, then place them on the map if possible.
    - Mountains and pits have exactly the given area but a random shape.
    - Mountains and pits are placed within the boundaries of map without overlap.
    - The shape generator code is covered with unit tests.
    - The shape placer code is covered with unit tests.

3. Resource placement\
    Place mineral and water units to previously empty fields, next to their preferences (mountains and pits, respectively) if possible.
    - Mineral and water units are placed next to their preferences (mountains and pits, respectively) if possible.
    - The resource placement code is covered with unit tests.

4. Configuration validator\
    Create a validator for the map configuration object that exits the program if the provided data cannot be placed safely into the given area.
    - There is a validator for the map configuration object that exits the program if the provided data cannot be placed safely into the given area.
    - The validation code is covered with unit tests.

5. Shaper strategies\
    Create different shape generating strategies, configurable by parameters.
    - There are different shape generating strategies.
    - The shape generating strategies can be configured using parameters which come from the map configuration object.

6. Save output\
    Save the generated map into a plain text file.
    - The generated map is saved into a plain text file specified in the configuration.

## Hints
- Find some example maps in the `resources` folder.
- Create all the shapes first before trying to arrange them on the map without overlapping. If the arrangement process comes to a dead end, start the arrangement over with the same shapes but in a different order. Set a limit for the retries.
- Try to write SOLID code. Discuss design choices within your team. The program must be open to extensions such as adding new terrain elements, resources, or generating strategies.

