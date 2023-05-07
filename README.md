# Text to Schematics: A Shap-e Adapter
<p align="center">
<img align="center" width="100" height="100" src="img/text-to-schematics-voice-text-block--blue-print--appicon-style-sleek-straight-sharp-lines-and-b-573419738-removebg-preview.png"/>
</p>
This project provides an adaptation of [OpenAI shap-e](https://github.com/openai/shap-e) 
to generate Minecraft schematics from text. The generated schematics can be used in-game for various purposes,
such as building structures, creating custom maps, and more.

## Google Colab
You can run it in Google Colab by clicking on the badge below:
[![Open All Collab](https://colab.research.google.com/assets/colab-badge.svg)](https://colab.research.google.com/drive/1KhvEpMtQN0flT5jC7GB0Q-Y6d_Q35lhR#scrollTo=xXS50Fti3IKl)

All the code needs to be in a code block.

## Example

<img align="left" src="img/img.png"/>

 `/sharpe shark`

<img align="rigth" src="img/img_1.png"/>

`/sharpe mario and luigi`

## Installation Requirements
- Google account is required.
- WorldEdit plugin is required.
- You need to have operator (op) privileges.

## How to Use
1. Enter the command `/sharpe <text>` in-game.
2. Wait for approximately one minute.
3. Use the command `//paste` to paste the result.

## How to Set Up
1. Install the plugin on your server.
2. Run all the Google Colab cells.
3. Copy the link (e.g., https://a5d49cfb6e8d642a77.gradio.live) from the last cell.
4. Use the command `/seturl https://a5d49cfb6e8d642a77.gradio.live`.

## Security
It's important to note that this plugin utilizes a third-party service (Google Colab) to process the text. 
As a result, it's necessary to be an operator to use this command to prevent unauthorized access to the server. Additionally,
it's recommended to only use this command with trusted text inputs to prevent any potential security risks.

## Credit
Project made by xam74er1 , you can reuse it for your project if you credit the orignal repo and the repo bellow . 

The project uses the [Faithful repository](https://github.com/Faithful-Resource-Pack/Faithful-Java-32x) to generate the block list, which can be adapted in the dedicated cell.

