# Spell Checker
A CLI Java App to Check for Spell Errors and Present Word Suggestions

## Description

The goal of this project is to provide a command-line application, written in Java, that interprets user word input, validates it, detects spelling errors and presents word suggestions to fix them.

To do this, we first implemented and tested a Bloom Filter and wrote functions for hash Murmur, Minhash and LSH.
Then, the SpellChecker application was developed, using the Bloom Filter to check if input words appear on the system's dictionaries and the Minhash and LSH to present alternative words when no matches are found based on string similarity.

## Repository Structure

\dicts - system dictionaries (PT and EN)

\docs - the written report on the work conducted is made available here

\src - contains the source code

## Authors

The authors of this repository are Filipe Pires and João Alegria, and the project was developed for the Probabilistic Methods for Informatics Engineering Course of the licenciate's degree in Informatics Engineering of the University of Aveiro.

For further information, please read our [report](https://github.com/FilipePires98/SpellChecker/blob/master/docs/Relat%C3%B3rio.pdf) or contact us at filipesnetopires@ua.pt or joao.p@ua.pt.
