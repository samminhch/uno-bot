# neural-network-uno-bot
This is a code demonstrating the power of artificial intelligence and how it mastered the art of Uno!™
## Table of Contents
1. [To-Do List](#to-do-list)
2. [About Code](#about-code)
3. [About Authors](#about-authors)
4. [License](#license)

## To-Do List:
### Making the Game:
#### Urgent:
- [x] Fix bug where if a player uses a reverse card and then draws in a 2-player match, it'll be their turn again.
- [x] Fix bug where if a player uses a draw card and then draws again, they'll get 2 cards again.  
- [x] Fix bug where if you don't have any playable cards and deny the first playable card drawn, it'll allow you to play any subsequently drawn card.  
- [x] Fix bug where if you play a draw card the opponent doesn't actually draw cards.
- [x] Fix bug where BufferedReader randomly crashes and breaks the game. ~~I still don't know how to fix that.~~ It was actually a scanner problem and I closed the scanner in the wild-card method.
- [x] Implement a stacking mechanic (i.e: if Player 1 plays a draw2 card and Player 2 plays a draw2 card and Player 1 has
  no more draw2 cards, Player 1 draws 4 cards.)
- [x] FINISH THE REST OF THE DARN GAME (it's basically done, so I'm marking it as such)
#### Not Urgent:
- [ ] Fix skipping bug (no urgent, as it only applies to game where more than 2 people are playing, which isn't what we're training our AI to do atm.)
- [ ] Make the stacking option configurable.
### Implementing the Neural Network:
- [ ] Find out how to implement it (Maybe ask Logan for help?)
- [ ] Rework game so that it'll be easier to make artificial intelligence for.

## About Code
This code is supposed to train an artificial to learn and master the Uno(r) game in order for me
to ~~beat my girlfriend/put it on my brag sheet~~ understand advanced concepts that a high school sophomore isn't supposed
to know at that age.

## About Authors
In this 2020-2021 School year, Logan and I are both high schoolers, with Logan being a senior and me a sophomore.
### Minh Nguyen
I'm the owner of this repository, and the person that came up with the idea to code an AI to master Uno!™. I also coded
the majority of the classes required for the code.
### Logan Simon
He's cool and a very good friend of mine.

## License
[MIT](https://choosealicense.com/licenses/mit/)