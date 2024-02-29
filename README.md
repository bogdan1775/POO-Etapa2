# Proiect GlobalWaves  - Etapa 2
# Croitoru Constantin-Bogdan
# Grupa 324CA

#Pentru rezolvare am folosit ca skelet rezolvarea oficiala pentru etapa1.

###Explicare Cod:
In clasa *Admin* am adaugat o lista numita albums de tipul clasei *Album* care retine albumele, o lista events de tipul clasei *Event* care retine evenimentele, o lista announcements de tipul clasei *Announcement* care retine anunturile si o lista merchs de tipul clasei *Merch* care retine merch-urile.

**Users**
Pentru utilizatori am creat 2 clase: clasa *Artist* pentru artist si *Host* pentru host. Aceste 2 clase mostenesc clasa *User*.

**Mod afisare**
Pentru comenzile de "showAlbums" si "showPodcasts" mi-am facut pachetul *display* care contine clasele *ShowAlbum* si *ShowPodcast*.

**Main**
In main se selecteaza ce comanda se executa. Pe langa comenzile de la etapa1 care au fost deja implementate in skelet, am implementat comenziile:
1.**switchConnectionStatus**- aceasta schimba statusul unui user normal. Daca userul primit este unul normal i se schimba statusul, iar daca userul este offline nu i se mai contorizeaza timpul.

2.**getOnlineUsers**- aceasta afiseaza userii normali care sunt online. Pentru aceasta se parcurge lista cu useri si se afiseaza cei care sunt online.

3.**addUser**- aceasta adauga un utilizator nou de tipul artist/host/normal. Pentru aceasta comanda mi-am facut doua clase: una pentru artist *Artist* si una pentru host *Host*.

4.**addAlbum** -aceasta adauga un album nou pentru un artist. Se verifica daca utilizatorul este artist, in caz pozitiv se cauta daca mai are un alt album cu numele asta. Daca nu are, se verifica daca in album are 2 melodii identice. Daca exista cel putin doua nu se adauga albumul, iar in caz contrar se creeaza albumul. Pentru album mi-am facut o clasa speciala *Album* pentru a retine campurile unui album.

5.**showAlbums** -aceasta afiseaza toate albumele unui artist cu melodiile din ele. Se parcurge lista cu albume si se retine intr-o lista de tipul *ShowAlbum* albumele artistului. Clasa *ShowAlbum* am creat-o special pentru a putea afisa albumele si melodiile unui artist.

6.**printCurrentPage** - aceasta afiseaza o anumita pagina in functie de tipul de utilizator si de tipul paginii. Pentru artist si host exista un singur tip de pagina, iar un user normal poate avea 2 tipuri de pagini sau se poate afla pe pagina unui artist sau host. In functie de tipul paginii prin *Visitor pattern* se afiseaza continul corespunzator.

7.**addEvent** -aceasta adauga un eveniment in memorie. Se verifica daca mai exista un eveniment cu acelasi nume, daca data este valida, iar in caz pozitiv se creeaza evenimentul. Pentru aceasta comanda am o clasa creata *Event* care retine datele despre un eveniment. Tot in aceasta clasa am si o metoda statica care verifica daca data unui eveniment este valida.

8.**addMerch** -aceasta adauga un merch. Se verifica daca mai exista alt merch cu numele acesta si daca are pretul pozitiv. Daca respecta aceste conditii se creeaza. Pentru aceasta comanda am creat clasa *Merch* care retine datele despre un merch.

9.**getAllUsers** -aceata afiseaza toti userii in ordinea urmatoare: user normal, artist si host. Se parcurge lista cu useri si se retine numele fiecaruia si se afiseaza lista.

10.**deleteUser** - aceasta sterge un utilizator din memorie. Inainte de a sterge un utilizator din memorie se verifica ca nimeni sa nu ii asculte o melodie, album, playlist sau o melodie dintr-un album sa fie intr-un playlist care se asculta in cazul artistului, sa nu ii asculte un podcast in cazul hostului si sa nu se afle pe pagina lui. Se declementeaza numarul de like-uri de la melodiile la care a dat like si se decrementeaza numarul de folowers de la playlisturile la care a dat follow.

11.**addPodcast** -aceasta adauga un podcast. Se verifica daca utilizatorul este un host si daca mai are un alt podcast cu acest nume. Daca nu are, se verifica ca un episod sa nu fie de 2 ori. Daca nu exista vreun episod de 2 ori se creeaza podcastul.

12.**addAnnouncement** -aceasta adauga un anunt nou. Se verifica daca utilizatorul este un host si sa nu exite alt anunt cu acelasi nume, iar apoi se creeaza si este retinut. Pentru aceasta comanda am creat clasa *Announcement* care retine datele despre un anunt.

13.**removeAnnouncement** -aceasta sterge un anunt. Se verifica daca utilizatorul este un host si are vreun anunt cu acest nume. Daca are se sterge din memeorie.

14.**showPodcasts** -aceasta afiseaza podcasturile unui host. Se retine intr-o lista de tipul *ShowPodcast* podcasturile si episoadele host-ului. Pentru aceasta am creat clasa *ShowPodcast* care retine datele ce trebuie afisate despre un podcast.

15.**removeAlbum** -aceasta sterge din memorie un album al unui artist. Se verifica daca utilizatorul este un artist si daca are un album cu acest nume. Daca exista, se verifica daca un user asculta acest album sau daca se asculta un playlist care contine melodia. Daca se respecta aceste conditii se sterge albumul.

16.**changePage** - aceasta schimba pagina unui user. Se verifica daca pagina pe care vrea sa o acceseze exista, iar in caz afirmativ se schimba.

17.**removeEvent** -aceasta sterge un eveniment. Se verifica daca userul este un artist si daca are un eveniment cu acest nume. Daca are se sterge evenimentul.

18.**removePodcast** -aceasta sterge un podcast al unui host. Se verifica daca userul este un host si are un podcast cu acest nume, iar apoi daca vreun utilizator asculta acest podcast. Daca nimeni nu il asculta atunci aceste este sters.

19.**getTop5Albums** -acesta afiseaza primele 5 albume cu cele mai multe like-uri. Pentru fiecare album se calculeaza numarul de like-uri care e egal cu suma like-urile melodiilor din album. Apoi se ordoneaza descrescator dupa numarul de like-uri, iar daca au acelasi numar de like-uri se ordoneaza alfabetic.

20.**getTop5Artists** -acesta afiseaza primii 5 artisi dupa numarul de like-uri. Pentru fiecare artist numarul de like-uri este numarul de like-uri ale fiecarui album. Creez o lista cu artistii si o ordonez descrescator dupa numarul de like-uri , iar daca au acelasi numar de like-uri alfabetic.

Pe langa noile comenzi a fost nevoie sa modific si cateva comenzi deja existe. Am modificat comanda **like**, astfel incat un utilizator offline sa nu poata da like, comanda **load** sa se poata incarca un album, comanda **search** pentru a cauta un album, un artist sau un host, comanda **select** pentru a selecta pagina unui artist sau a unui host si comanda **shuffle** pentru a o putea aplica si pe un album.


###Design pattern
**Visitor pattern**
Pentru camanda "prinCurrentPage* mi-am facut un Visitor pattern care in metoda visit afiseza pagina corespunzatoare, adica daca e artist pagina artistului, daca e host pagana hostului, iar daca e user pagina corespunzatoare lui adica HomePage sau LikedContentPage in cazul in care nu se afla pe pagina unui artist sau host, iar daca se afla se afiseaza pagina artistului/hostului.

**Singleton pattern**
In clasa LibraaryInput mi-am facut un Singleton ca libraria sa fie initializa o singura data.





