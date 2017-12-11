# Treść zadania #
1. Przeczytaj ogólny oraz szczegółowy składni pliku bazy bibliograficznej BibTeX
2. Zapoznaj się z przykładowymi plikami BibTeX: 1, 2, 3
3. Opracuj zestaw klas oraz odpowiednie algorytmy tak, aby Twój program:
    1. Wczytywał zawartość pliku bazy bibliograficznej BibTeX (*.bib)
    2. Konwertował postać tekstową na obiektową przy użyciu zaimplementowanego przez Ciebie prostego parsera - **nie można korzystać z gotowych rozwiązań** typu: generator parserów (ANTLR, jacc, CUP itp.), parser BibTeX (Publy, JabRef, JBibTeX itp.) lub dowolnych innych
    2. Używając postaci obiektowej, czyli Twojego modelu dokumentu BibTeX, wyszukiwał, a następnie wyświetlał dane wynikowe jako tabele z obramowaniem w postaci znaków ASCII.
4. Parser, dla wejściowego dokumentu BibTeX ma:
    - Generować obiektową postać dokumentu BibTeX
    - Obsługiwać pola (wymagane, opcjonalne, ignorowane):
        - Generować ostrzeżenie w postaci wyjątku w przypadku braku pól wymaganych
        - Uwzględniać informacje zawarte w polach opcjonalnych
        - Pomijać informacje zawarte w polach ignorowanych
    - Uwzględniać zmienne napisowe - obsługiwać deklarację `@string`
    - Usuwać z tekstu wszystkie inne, zbędne elementy:
        - ograniczniki wartości pól
        - deklaracje `@comment` oraz `@preamble`
        - fragmenty niezawierające ani rekordów, ani deklaracji
5. Program ma dostarczać następujących funkcjonalności:
    - Wyświetlanie wszystkich pozycji literaturowych (publikacji)
    - Wyszukiwanie publikacji należących do podanych kategorii, np. wszystkie typu 'book' oraz 'article'
    - Wyszukiwanie publikacji podanego autora lub autorów
    - Wyświetlanie nazw osób ma się odbywać następująco: każdy autor w osobnej linii (jak w przykładzie); najpierw jego imię, a potem nazwisko
    - Wyświetlanie pomocy (opis opcji i sposobu uruchamiania) gdy program zostanie wywołany bez argumentów
6. Argumenty programu:
    - Ścieżka dostępu pliku BibTeX
    - Nazwiska autorów
    - Nazwy kategorii
    - Znak, z którego ma zostać utworzone obramowanie
7. Proszę zaprojektować swój system tak, aby w przyszłości dało się go, po niewielkich modyfikacjach, wzbogacić o możliwość wyszukiwania w oparciu o inne kryteria, np. wyszukiwanie wszystkich publikacji wydanych w określonym roku lub latach
8. Postaraj się użyć (o ile potrafisz), co najmniej jednego, wzorca projektowego