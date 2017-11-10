# bayesian-inference

***How to compile from source:***

- The source files will be compiled using `javac` with options `-g -cp . -d ./build` per project specifications. After compilation, the new .class files can be run with `java -cp ./build` (see `./Makefile` and <u>How to execute program</u> for further specifications or exact commands).

- **Compile exact inference** (ExactInferer)
  - Run `make exact` in the project directory. This will only compile the program.
- **Compile approximate inference** (ApproxInferer)
  - Run `make approx` in the project directory. This will only compile the program.
- **Remove compiled files**
  - Run `make clean` in the project directory. This will delete all compiled .class files.

***Argument formatting {ARGS}:***

- **ExactInferer** arguments are formatted as follows:
  - `filename.[xml/bif] QUERYVAR EVIDENCE1 value1 EVIDENCE2 value2 …`
  - Example: **{ARGS}** = `dog-problem.xml bowel-problem hear-bark true`
- **ApproxInferer** arguments are formatted as follows:
  - `#SAMPLES filename.[xml/bif] QUERYVAR EVIDENCE1 value1 EVIDENCE2 value2 …`
  - Example: **{ARGS}** = `10000 insurance.bif SocioEcon VehicleYear Older DrivingSkill SubStandard`

***How to execute program:***

- **Execute compiled ExactInferer**
  - `java -cp ./build exact/App {ARGS} `
- **Execute compiled ApproxInferer**
  - `java -cp ./build approx/App {ARGS}`
