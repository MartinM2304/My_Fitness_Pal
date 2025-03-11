# Cleand-Code-course-FMI

The Gameplay.cpp and .h files are not fully refactored as the time was limited. All magic numbers and strings must be converted to defines and used this way. Other thing that is not fully following the clean code rules is that some methods (example:bool Gameplay::canMove) are more than 20 lines (canMove is 30) but this is because all the logic in this method is connected and would not be benificial to distribute it to multiple methods.
