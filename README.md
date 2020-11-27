# 3d-Four-in-a-Row

This was a project that both my friend Naoto and I made seperate bots for.
Here is a link to a concise video about it: ____

In a game called 3D Four in a Row, we created bots to choose the best move for the next turn based on a minimax algorithm.

# Rules
The game is set on a 4x4x4 grid, and you simply drop in your color of sphere each time it is your turn, similar to four in a row. The first person to achieve four spheres in a row wins. Here's a link of what the board looks like: https://images-na.ssl-images-amazon.com/images/I/5101aIKtYaL._AC_SX425_.jpg

# Game Theory
There isn't much concrete theory for 3D Four in a Row, but there are certain spaces that can clearly be shown to be better than others in certain cases. For instance, corner points are always advantageous to take when open, because every corner has 5 potential winning directions from it. Additionally, having multiple streaks that complement each other is advantageous (by complementing, I mean that it forces the enemy into stopping one win, which allows the player to achieve a different win). However, having multiple streaks of spheres that converge on the same winning point could be viewed as useless, because you could stack all your wins on one point just to have it blocked in one turn. These were the main points taken into account, and were used in the minimax feature that allowed the bot to work.

# Minimax
Minimax is an algorithm often used for chess engines and other games, which essentially looks at all possible choices that you can make, assuming your opponent also plays perfectly, and then devises a set of steps that will get you to achieve the best score possible. While this sounds unbeatable, its main fallbacks are shortsightedness due to shortage of memory and time, and that the scoring algorithm is created by humans, and if there is not a concrete game theory (like in this case), the scoring algorithm will not perfectly lead the bot to a winning move.

# Testing it yourself
The easiest way to do this would just be to run the play.java file. Unfortunately, it isn't super friendly as of yet, as there is no UI. Instead you play in the terminal, and must visualize the board yourself. 
