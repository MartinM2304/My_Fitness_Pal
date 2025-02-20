#include "Gameplay.h"
#include <iostream>

#define MOVE_FROM_LETTER 5
#define MOVE_TO_LETTER 8
#define MOVE_FROM_NUMBER 6
#define MOVE_TO_NUMBER 9

#define PIECE_BLOCKING_PATH "There's a piece blocking your move!"

#define PAWNS_CAN_MOVE_MORE_THAN_TWO "Pawns can move 2 squares forward only in their first move"
#define PAWNS_CANT_MOVE_MORE_THAN_TWO "Pawns can't move more than 2 squares forward their first move."
#define PAWNS_MOVE_ONLY_FORWARD "Pawns can only move forward"
#define PAWNS_MOVE_ONLY_ONE_COLUMN_FORWARD "Pawns can't move more than one column forward"
#define PAWNS_MOVE_DIAGONAL_ENEMY_PIECE "There must be an enemy piece on that position for you to move there."
#define PAWNS_MOVE_ENEMY_BLOCKING "An enemy piece is blocking your move!"

#define KING_CAN_MOVE_ONE_SQ "Kings may only move one square"

#define QUEEN_CAN_MOVE_LIKE_BK "Queens can move like rooks and bishops only"

#define KNIGHT_MOVE_L_SHAPE "Knights can move only L-shape"

#define BISHOPS_MOVE_DIAGONAL "Bishops can move only diagonally"

#define ROOKS_MOVE_HOR_VERT "Rooks can move only horizontally or vertically"

Gameplay::Gameplay()
{
}

Gameplay::~Gameplay()
{
}



bool Gameplay::isMoveCommandValid(String move)
{
	//example command: move d2 d4 
	if (move.getLen() != 10) {
		std::cout << "Invalid move format!" << std::endl;
		return false;
	}
	else if (move[0] != 'm' || move[1] != 'o' || move[2] != 'v' || move[3] != 'e' || move[4] != ' ' || move[7] != ' ') {
		std::cout << "Invalid move format!" << std::endl;
		return false;
	}
	else if (move[5] < 'a' || move[5] > 'h' || move[6] < '1' || move[6] > '8' || move[8] < 'a' || move[8] > 'h' || move[9] < '1' || move[9] > '8') {
		std::cout << "Invalid move format!" << std::endl;
		return false;
	}
	return true;
}

bool Gameplay::isDestinationOccupiedByEnemy(Board board[8][8], int toNumber, int toLetter, int enemyPlayer)const {
	return (board[toNumber][toLetter].piece != nullptr && board[toNumber][toLetter].piece->getPlayer() == enemyPlayer);
}

void Gameplay::handleCapture(Board board[8][8], int toNumber, int toLetter) {
	std::cout << "You have taken the enemy's " << board[toNumber][toLetter].piece->getName() << "!" << std::endl;
	if (board[toNumber][toLetter].piece->getName() == "King") {
		isKingDead = true;
	}
}

bool Gameplay::isPathClear(Board board[8][8], int fromNumber, int fromLetter, int toNumber, int toLetter)
{
	int rowStep = (toNumber > fromNumber) ? 1 : ((toNumber < fromNumber) ? -1 : 0);
	int colStep = (toLetter > fromLetter) ? 1 : ((toLetter < fromLetter) ? -1 : 0);
	int row = fromNumber + rowStep;
	int col = fromLetter + colStep;

	while (row != toNumber || col != toLetter) {
		if (board[row][col].piece != nullptr) {
			return false;
		}
		row += rowStep;
		col += colStep;
	}
	return true;
}

void Gameplay::movePiece(Board board[8][8], int fromNumber, int fromLetter, int toNumber, int toLetter) {
	board[toNumber][toLetter].piece = board[fromNumber][fromLetter].piece;
	board[fromNumber][fromLetter].piece = nullptr;
	std::cout << "Move successful!" << std::endl;
}

int Gameplay::validateMoveLetter(char ch)const {
	if (ch > 'h') {
		ch = 'h';
	}
	return ch - 'a';
}

int Gameplay::validateMoveNumber(char ch)const {
	if (ch > '8') {
		ch = '8';
	}
	return ch - '1';
}

bool Gameplay::validateMove(Board board[8][8], int fromLetter, int toLetter, int fromNumber, int toNumber, int currentPlayer){
	if (board[fromNumber][fromLetter].piece == nullptr) {
		std::cout << "There's no piece at that location." << std::endl << std::endl;
		return false;
	}

	if (board[fromNumber][fromLetter].piece != nullptr && board[fromNumber][fromLetter].piece->getPlayer() != currentPlayer) {
		std::cout << "That's not your piece!" << std::endl << std::endl;
		return false;
	}

	if (board[toNumber][toLetter].piece != nullptr && board[toNumber][toLetter].piece->getPlayer() == currentPlayer) {
		std::cout << "You already have a piece there." << std::endl;
		return false;
	}
}

bool Gameplay::validatePawnMove(Board board[8][8], int fromNumber,  int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer) {
	int startingRow = (currentPlayer == 1) ? 6 : 1;
	int forwardDirection = (currentPlayer == 1) ? -1 : 1;
	int enemyPlayer = (currentPlayer == 1) ? 2 : 1;

	if (fromNumber != startingRow && abs(rowDifference) == 2) {
		std::cout << PAWNS_CAN_MOVE_MORE_THAN_TWO << std::endl;
		return false;
	}
	if (fromNumber == startingRow && abs(rowDifference) > 2) {
		std::cout << PAWNS_CANT_MOVE_MORE_THAN_TWO << std::endl;
		return false;
	}
	if (rowDifference * forwardDirection <= 0) {
		std::cout << PAWNS_MOVE_ONLY_FORWARD << std::endl;
		return false;
	}
	if (abs(columnDifference) > 1) {
		std::cout << PAWNS_MOVE_ONLY_ONE_COLUMN_FORWARD << std::endl;
		return false;
	}
	if (abs(columnDifference) == 1 && abs(rowDifference) == 1 && board[toNumber][toLetter].piece == nullptr) {
		std::cout << PAWNS_MOVE_DIAGONAL_ENEMY_PIECE << std::endl;
		return false;
	}
	if (abs(rowDifference) == 1 && columnDifference == 0 && isDestinationOccupiedByEnemy(board, toNumber, toLetter, enemyPlayer)) {
		std::cout << PAWNS_MOVE_ENEMY_BLOCKING << std::endl;
		return false;
	}
	if (abs(rowDifference) == 1 && abs(columnDifference) == 1 && isDestinationOccupiedByEnemy(board, toNumber, toLetter, enemyPlayer)) {
		handleCapture(board, toNumber, toLetter);
		movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
		return true;
	}
	movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
	return true;
}

bool Gameplay::validateKingMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)
{
	if (abs(rowDifference) > 1 || abs(columnDifference) > 1) {
		std::cout << KING_CAN_MOVE_ONE_SQ << std::endl;
		return false;
	}

	if (isDestinationOccupiedByEnemy(board, toNumber, toLetter, currentPlayer)) {
		handleCapture(board, toNumber, toLetter);
		movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
		return true;
	}
	movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
	return true;
}

bool Gameplay::validateQueenMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)
{
	if (abs(rowDifference) != abs(columnDifference) && rowDifference != 0 && columnDifference != 0) {
		std::cout << QUEEN_CAN_MOVE_LIKE_BK << std::endl;
		return false;
	}

	if (!isPathClear(board, fromNumber, fromLetter, toNumber, toLetter)) {
		std::cout << PIECE_BLOCKING_PATH << std::endl;
		return false;
	}

	if (isDestinationOccupiedByEnemy(board, toNumber, toLetter, currentPlayer)) {
		handleCapture(board, toNumber, toLetter);
		movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
		return true;
	}

	movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
	return true;
}

bool Gameplay::validateKnightMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)
{
	if (abs(rowDifference) * abs(columnDifference) != 2) {
		std::cout << KNIGHT_MOVE_L_SHAPE << std::endl;
		return false;
	}

	if (isDestinationOccupiedByEnemy(board, toNumber, toLetter, currentPlayer)) {
		handleCapture(board, toNumber, toLetter);
		movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
		return true;
	}

	movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
	return true;
}

bool Gameplay::validateBishopMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)
{
	if (abs(rowDifference) != abs(columnDifference)) {
		std::cout << BISHOPS_MOVE_DIAGONAL << std::endl;
		return false;
	}

	if (!isPathClear(board, fromNumber, fromLetter, toNumber, toLetter)) {
		std::cout << PIECE_BLOCKING_PATH << std::endl;
		return false;
	}

	if (isDestinationOccupiedByEnemy(board, toNumber, toLetter, currentPlayer)) {
		handleCapture(board, toNumber, toLetter);
		movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
		return true;
	}

	movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
	return true;
}

bool Gameplay::validateRookMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)
{
	if (rowDifference != 0 && columnDifference != 0) {
		std::cout << ROOKS_MOVE_HOR_VERT << std::endl;
		return false;
	}

	if (!isPathClear(board, fromNumber, fromLetter, toNumber, toLetter)) {
		std::cout << PIECE_BLOCKING_PATH << std::endl;
		return false;
	}

	if (isDestinationOccupiedByEnemy(board, toNumber, toLetter, currentPlayer)) {
		handleCapture(board, toNumber, toLetter);
		movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
		return true;
	}

	movePiece(board, fromNumber, fromLetter, toNumber, toLetter);
	return true;
}

char Gameplay::getPieceChar(String name, int player) const
{
	if (name == "Pawn")       return (player == 1) ? 'P' : 'p';
	if (name == "King")       return (player == 1) ? 'K' : 'k';
	if (name == "Queen")      return (player == 1) ? 'Q' : 'q';
	if (name == "Rook")       return (player == 1) ? 'R' : 'r';
	if (name == "Knight")     return (player == 1) ? 'N' : 'n';
	if (name == "Bishop")     return (player == 1) ? 'B' : 'b';
	return ' ';
}

bool Gameplay::canMove(String move, Board board[8][8],int currentPlayer) {
	int fromLetter = validateMoveLetter(move[MOVE_FROM_LETTER]);
	int toLetter = validateMoveLetter(move[MOVE_TO_LETTER]);
	int fromNumber = validateMoveNumber(move[MOVE_FROM_NUMBER]);
	int toNumber = validateMoveNumber(move[MOVE_TO_NUMBER]);
	
	if (!validateMove(board, fromLetter, toLetter, fromNumber, toNumber,currentPlayer)) {
		return false;
	}
	int rowDifference = fromNumber - toNumber;
	int columnDifference = fromLetter - toLetter;
	String pieceName = board[fromNumber][fromLetter].piece->getName();

	if (pieceName == "Pawn") {
		return validatePawnMove(board, fromNumber, toNumber, fromLetter, toLetter, rowDifference, columnDifference, currentPlayer);
	}
	else if (pieceName == "King") {
		return validateKingMove(board, fromNumber, toNumber, fromLetter, toLetter, rowDifference, columnDifference, currentPlayer);
	}
	else if (pieceName == "Queen") {
		return validateQueenMove(board, fromNumber, toNumber, fromLetter, toLetter, rowDifference, columnDifference, currentPlayer);
	}
	else if (pieceName == "Knight") {
		return validateKnightMove(board, fromNumber, toNumber, fromLetter, toLetter, rowDifference, columnDifference, currentPlayer);
	}
	else if (pieceName == "Bishop") {
		return validateBishopMove(board, fromNumber, toNumber, fromLetter, toLetter, rowDifference, columnDifference, currentPlayer);
	}
	else if (pieceName == "Rook") {
		return validateRookMove(board, fromNumber, toNumber, fromLetter, toLetter, rowDifference, columnDifference, currentPlayer);
	}
	else {
		return false;
	}
}

void Gameplay::FillBoard()
{ 
	for (int i = 0; i < 28; i++){
		for (int j = 0; j < 65; j++){
			gameField[i][j] = ' ';
		}
	}
}
void Gameplay::PaintBoard()
{
	for (int i = 3; i < 27; i++){
		for (int j = 7; j < 63; j++){
			if ((((i > 2 && i < 6) || (i > 8 && i < 12) || (i > 14 && i < 18) || (i > 20 && i < 24))
				&& ((j > 6 && j < 14) || (j > 20 && j < 28) || (j > 34 && j < 42) || (j > 48 && j < 56))) ||
				(((i > 5 && i < 9) || (i > 11 && i < 15) || (i > 17 && i < 21) || (i > 23 && i < 27))
					&& ((j > 13 && j < 21) || (j > 27 && j < 35) || (j > 41 && j < 49) || (j > 55 && j < 63))))
				gameField[i][j] = 219;
		}
	}
}
void Gameplay::PrintBorders() {
	for (int i = 3; i < 27; i++){
		gameField[i][5] = 186;
		gameField[i][64] = 186;
	}
	gameField[2][5] = 201;
	gameField[2][64] = 187;
	gameField[27][64] = 188;
	gameField[27][5] = 200;
	for (int j = 6; j < 64; j++){
		gameField[2][j] = 205;
		gameField[27][j] = 205;
	}
}
void Gameplay::PrintNumbering()
{
	int k = 0;

	//column naming
	for (int j = 10; j < 63; j += 7){
		gameField[1][j] = 65 + k;
		k++;

	}
	//line numbering
	for (int i = 4; i < 27; i += 3){{
			gameField[i][3] = 41 + k;
			k++;
		}
	}
}
void Gameplay::FigureSpaceCleaning()
{
	//freeing space for figures
	for (int i = 4; i < 27; i += 3){
		for (int j = 10; j < 63; j += 7){
			gameField[i][j - 1] = '  ';
			gameField[i][j] = '  ';
			gameField[i][j + 1] = '  ';
		}
	}
}


void Gameplay::PlacePieces(Board board[8][8])
{
	const int rowCoords[8] = { 4, 7, 10, 13, 16, 19, 22, 25 };
	const int colulmnCoords[8] = { 10, 17, 24, 31, 38, 45, 52, 59 };
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			int row = rowCoords[i], col = colulmnCoords[j];
			if (!board[i][j].piece) {
				gameField[row][col] = ' ';
				continue;
			}
			String name = board[i][j].piece->getName();
			int player = board[i][j].piece->getPlayer();
			gameField[row][col] = getPieceChar(name,player);
		}
	}
	for (int i = 0; i < 28; i++) {
		for (int j = 0; j < 65; j++) std::cout << gameField[i][j];
		std::cout << std::endl;
	}
}
void Gameplay::PrintBoard(Board board[8][8])
{
	FillBoard();
	PrintNumbering();
	PaintBoard();
	FigureSpaceCleaning();
	PrintBorders();
	PlacePieces(board);
}

void Gameplay::initPieces(int player, int pawnRow, int backRow) {
	for (int i = 0; i < 8; i++) {
		board[pawnRow][i].piece = new Pawn("Pawn", player);
	}

	board[backRow][0].piece = new Rook("Rook", player);
	board[backRow][1].piece = new Knight("Knight", player);
	board[backRow][2].piece = new Bishop("Bishop", player);
	board[backRow][3].piece = new Queen("Queen", player);
	board[backRow][4].piece = new King("King", player);
	board[backRow][5].piece = new Bishop("Bishop", player);
	board[backRow][6].piece = new Knight("Knight", player);
	board[backRow][7].piece = new Rook("Rook", player);
}

void Gameplay::init() {
	initPieces(1, 6, 7);
	initPieces(2, 1, 0);
}

void Gameplay::handleCommand(String command, int currentPlayer)
{
	if (command == "help") {
		printHelp();
		currentPlayer = (currentPlayer == 1) ? 2 : 1;
	}
	else if (command == "undo") {
	}
	else if (command == "exit") {
		exit(0);
	}
	else {
		processMove(command, currentPlayer);
	}
}

void Gameplay::printHelp()
{
	std::cout << "-----------------Help menu-----------------" << std::endl;
	std::cout << "Possible commands: " << std::endl;
	std::cout << "undo -> returns the game to the state before the last move." << std::endl;
	std::cout << "exit -> exits the game." << std::endl;
	std::cout << "move x1y1 x2y2 -> if possible, move the figure from position (x1, y1) to position (x2, y2)" << std::endl;
	std::cout << "example move command ---> move d2 d4" << std::endl;
}

void Gameplay::processMove(String command, int currentPlayer)
{
	while (!isMoveCommandValid(command)) {
		std::cin >> command;
	}
	while (!canMove(command, board, currentPlayer) || !isMoveCommandValid(command)) {
		std::cout << "Please enter your move again!\n";
		std::cin >> command;
	}
}

void Gameplay::PlayGame()
{
	init();

	int currentPlayer = 2;
	String command;

	while (true) {
		currentPlayer = (currentPlayer == 1) ? 2 : 1;
		PrintBoard(board);

		std::cout << "\nIt is Player " << currentPlayer << "'s turn. Please enter your command:\n";
		std::cin >> command;

		handleCommand(command, currentPlayer);

		if (isKingDead) {
			std::cout << "Player " << currentPlayer << " wins!\n";
			break;
		}
	}
}