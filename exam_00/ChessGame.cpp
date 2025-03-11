#include <iostream>
#include "Board.h"
#include "Gameplay.h"
#include <string>
#include <cmath>
#include <Windows.h>
#include <fstream>

int main() {
	HWND console = GetConsoleWindow();
	RECT r;
	GetWindowRect(console, &r);
	MoveWindow(console, r.left, r.top, 800, 700, TRUE);
	Gameplay game;
	game.PlayGame();
}