**Deployment:**

    1. Downloaden und installieren Sie die Docker Plattform von https://www.docker.com/get-started.
    
    2. Starten Sie Docker auf Ihrem System.
    
    3. Öffnen Sie ein Terminal im Projekt Ordner "g2t4".
    
    4. Geben Sie den Befehl "docker-compose up" ein.
    
    5. Das Projekt wird installiert und alle JUnit Tests ausgeführt.
    
    6. Sollte das Projekt nach erfolgreich ausgeführten Tests mit einem Fehler beenden, geben Sie erneut "docker-compose up" ein.



**Einbindung und Konfiguration eines TimeFlip Würfels**
Für die Einbindung wird empfohlen den TimeFlip Würfel "TimeFlip1" zu nennen.

    1. Den Würfel mit "sudo hcitool lescan" finden.
    
    2. Mit dem Würfel verbinden "sudo gatttool -b <Mac> -I"
    
    3. "Connect" schreiben
    
    4. "primary" eingeben und den Handle der Characteristic "0x2A00" suchen.
    
    5. Den Namen "TimeFlip1" als hex eingeben mit "char-write-req <handle> <data>" (54696d65466c697031 <- TimeFlip1 in Hex)
    
    6. "exit" schreiben und kurze Zeit warten bis der Name übernommen ist.
    
    7. (Bei Batterieausfall kann es vorkommen, dass dieser Vorgang wiederholt werden muss)
    
    8. Die Würfelseiten-Nummerierung die der TimeFlip hat in das Programm einfügen. (Siehe Testcase RC Raspberry Pi.2)