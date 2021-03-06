# <a name="anchorTop"></a>Desafio Bootcamp everis Kotlin Developer
> Projeto baseado nos desafios do **Bootcamp everis Kotlin Developer**.



### Oi,

Este é um projeto referente ao exercício proposto no **Bootcamp everis Kotlin Developer** onde fomos convidados a recriar a calculadora IMC ***(BMI - Body Mass Index)*** usada em uma das aulas.

Como plus para esse projeto, foi adicionado um novo feature para calcular o Percentual de Gordura baseada no resultado do IMC - ***(BFP - Body Fat Percentage based on BMI)*** incluindo a arquitetura **MVVM**.



## O projeto tem como base

- Kotlin
- MVI (MVVM) Architecture (Arquitetura em uso:MVI Model View Intent qualquer atualização da IU é definida por um novo estado. Seria um componente a mais usado na arquitetura MVVM do que uma arquitetura propriamente dita.
- View Binding
- SharedPreferences
- Coroutine Scope, Flow
- ViewFlipper
- DayNight Theme
- Third-party library GaugeView (http://www.sccomponents.com/)
- Guidelines - Material Design



| EXERCÍCIO | NÍVEL | SOLUÇÃO |
| :--: | :--: | :--: |
| [Fórmulas](#anchor01) | Básico | [Lógica](app/src/main/java/com/fappslab/bmi/repository/MainRepository.kt) |




 > ### [Download APK](https://github.com/F4bioo/BMI/raw/master/app/release/app-release.apk)



### Tela sugerida

Suggested screen

<img src="suggested.png" width="30%"></img>



### Minha Implementação

My implementation

<img src="screen_capture1.png" width="30%"></img>
<img src="screen_capture2.png" width="30%"></img>
<img src="screen_capture3.png" width="30%"></img>
<img src="screen_capture4.png" width="30%"></img>

# <a id="anchor01"></a>Fórmulas 

### [voltar ao topo](#anchorTop)

## **BMI - Body Mass Index**
Índice de Massa Corporal
Fórmula para adultos
<img src="BMI.png" width="30%"></img>

```sh
BMI = (weight(kg) / height (m²))
```



## **BFP - Body Fat Percentage based on BMI**
Percentual de Gordura baseada no IMC
Fórmula para adultos

```sh
gender = if female 0; if male 1;
BFP = ((1.2 * bmi) + (0.23 * age) - (10.8 * gender) - 5.4)
```
