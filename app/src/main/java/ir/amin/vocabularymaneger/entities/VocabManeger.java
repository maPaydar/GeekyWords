package ir.amin.vocabularymaneger.entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Scanner;

/**
 * Created by amin on 9/8/16.
 */

public class VocabManeger {

    private Document doc;

    public VocabManeger(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        inputStream.close();
        doc = Jsoup.parse(sb.toString(), "UTF-8");
    }

    public void addWord(Word word) throws FileNotFoundException, UnsupportedEncodingException {
        Element words = getWordsElement();
        Element newWord = doc.createElement("word");
        newWord.append(word.getXmlWord());
        words.appendChild(newWord);
        saveFile("vocab.vocab", doc.outerHtml());
    }

    private String getTextOfChildByTagName(Element parent, String tagName) {
        return parent.getElementsByTag(tagName).get(0).text();
    }

    private Word getWordFromElement(Element e) {
        Word w = new Word(getTextOfChildByTagName(e, "value"),
                getTextOfChildByTagName(e, "translate"),
                getTextOfChildByTagName(e, "definition"),
                getTextOfChildByTagName(e, "synonym"),
                getTextOfChildByTagName(e, "opposite"),
                getTextOfChildByTagName(e, "example"));
        return w;
    }

    public Word[] getAllWords() {
        Word[] ws = null;
        Element words = getWordsElement();
        Elements wordsChilds = words.children();
        ws = new Word[wordsChilds.size()];
        for (int i = 0; i < wordsChilds.size(); i++) {
            Element e = wordsChilds.get(i);
            Word w = getWordFromElement(e);
            ws[i] = w;
        }
        return ws;
    }

    public Word getWordByValue(String wordValue) {
        Element words = getWordsElement();
        Elements wordsChilds = words.children();
        for (int i = 0; i < wordsChilds.size(); i++) {
            Element e = wordsChilds.get(i);
            if (getTextOfChildByTagName(e, "value").equals(wordValue)) {
                return getWordFromElement(e);
            }
        }
        return null;
    }

    private Element getWordsElement() {
        Elements body = doc.select("body");
        Element words = body.get(0).child(0);
        return words;
    }

    private void saveFile(String file, String content) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(new File(file), "UTF-8");
        pw.print(content);
        pw.close();
    }

    public void getHtmlOutPut() throws FileNotFoundException, UnsupportedEncodingException {
        Document outputDoc = Jsoup.parse(getFileContent("htmlFrame.html"));
        Element wrapper = outputDoc.getElementsByClass("wrapper").get(0);
        wrapper.empty();
        String htmlStr = "";
        Word[] ws = getAllWords();
        for (int i = 0; i < ws.length; i++) {
            Word w = ws[i];
            htmlStr += "<div class=\"word\">\n" +
                    "        <div class=\"row\">\n" +
                    "            <div class=\"left-col word-value\">" + w.word + "</div>\n" +
                    "            <div class=\"right-col\">" + w.translate + "</div>\n" +
                    "        </div>\n" +
                    "        <div class=\"row\">\n" +
                    "            <div class=\"left-col\">DEF&nbsp;:&nbsp;</div>\n" +
                    "            " + w.definition + "<br>" +
                    "        </div>" +
                    "        <div class=\"row\">\n" +
                    "            <div class=\"left-col\">SYN&nbsp;:&nbsp;</div>\n" +
                    "            " + w.synonym + "<br>" +
                    "        </div>\n" +
                    "        <div class=\"row\">\n" +
                    "            <div class=\"left-col\">OPS&nbsp;:&nbsp;</div>\n" +
                    "            " + w.opposite + "<br>" +
                    "        </div>\n" +
                    "        <div class=\"row\">\n" +
                    "            <div class=\"left-col\">EXM&nbsp;:&nbsp;</div>\n" +
                    "            " + w.example + "<br>" +
                    "        </div>\n" +
                    "    </div>";
        }
        wrapper.append(htmlStr);
        saveFile("htmlFrame.html", outputDoc.outerHtml());
    }

    private String getFileContent(String file) throws FileNotFoundException {
        String fileContent = "";
        Scanner sc = new Scanner(new File(file));
        while (sc.hasNextLine()) {
            fileContent += sc.nextLine();
        }
        return fileContent;
    }
}
