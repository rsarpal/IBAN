**Description**

The code generates valid Finnish IBANs (Banking Account numbers)


---
## Jar
iban.jar exists in /out/artifacts/iban folder

## Package
com.rsarpal.IBAN;

---

## Example

```java 
IBAN newIban = new IBAN("FI");
 for (int i = 0; i < 100; i++)
    System.out.println(newIban.ibanGenerator(newIban.finAccGenerator()));
```
