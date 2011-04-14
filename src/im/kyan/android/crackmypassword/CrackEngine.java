/*
 * -- CrackEngine.java --
 * 
 * Copyright 2011, Kyan He <kyan.ql.he@gmail.com>
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Modified:
 * Kyan He <kyan.ql.he@gmail.com> @ Apr 8, 2011
 */

/***************************************************************
 ** Script by Small Hadron Collider
 ** http://www.smallhadroncollider.com
 ** Distributed under a Creative Commons by-sa License
 ** http://creativecommons.org/licenses/by-sa/3.0/
 ** WARNING: Top 500 passwords array at the bottom contains
 ** naughty words. Don"t look at it if you"re easily
 ** offended.
 ***************************************************************/
package im.kyan.android.crackmypassword;

import android.util.Log;

/**
 * @author kyan
 */
public class CrackEngine {
    
    private static final String TAG = "CrackEngine";
    
    // Usage: Pass the password string to passwordStrength and string with time
    // it will take is returned
    // @see http://en.wikipedia.org/wiki/Password_strength
    public static double calculatePasswordStrength(String password) {
        // Get password length
        int length = password.length();
        
        // Check password against common passwords of that length to see if it"s
        // in top 500
        if (length > 2 && length < 9) {
            for (int i = 0; i < sDictionary[length].length; i++) {
                if (password.toLowerCase().equals(sDictionary[length][i])) {
                    return -1;
                }
            }
        }
        
        // Calculations per second. Ten million is roughly the number a decent
        // PC could manage uninhibited
        int calculationsPerSecond = 10000000;
        
        // Keep track of how many character sets are used
        int possibleCharacters = 0;
        
        {
            boolean haveLowercase = false;
            boolean haveUppercase = false;
            boolean haveDigit = false;
            boolean haveSymbol = false;
            
            for (int i = 0; i < length; i++) {
                
                char c = password.charAt(i);
                
                // lowercase
                if (!haveLowercase && Character.isLowerCase(c)) {
                    possibleCharacters += 26;
                    haveLowercase = true;
                }
                
                // uppercase
                if (!haveUppercase && Character.isUpperCase(c)) {
                    possibleCharacters += 26;
                    haveUppercase = true;
                }
                
                // digit
                if (!haveDigit && Character.isDigit(c)) {
                    possibleCharacters += 10;
                    haveDigit = true;
                }
                
                // symbol
                if (!haveSymbol) {
                    switch (c) {
                    case '~':
                    case '!':
                    case '@':
                    case '#':
                    case '$':
                    case '%':
                    case '^':
                    case '&':
                    case '*':
                    case '(':
                    case ')':
                    case '_':
                    case '-':
                    case '?':
                        possibleCharacters += 14;
                        haveSymbol = true;
                    }
                }
            }
        }
        
        // Work out the number of possible combinations: possible characters to
        // the power of the password length
        double possibleCombinations = Math.pow(possibleCharacters, length);
        
        // Divide the number of possible combinations by the calculations a PC
        // can do per second
        double computerTimeInSecs = possibleCombinations
                / calculationsPerSecond;
        
        Log.d(TAG, "len: " + length + " comnination: " + possibleCharacters
                + String.format("secs: %.9f", computerTimeInSecs));
        return computerTimeInSecs;
    }
    
    /**
     * Common password dictionary
     */
    static final private String[][] sDictionary = {
            // for padding
            {},
            // #1 character
            {},
            // #2 characters
            {},
            // #3 characters
            { "god", "sex" },
            // #4 characters
            { "1234", "cool", "1313", "star", "golf", "bear", "dave", "pass",
                    "aaaa", "6969", "jake", "matt", "1212", "fish", "fuck",
                    "porn", "4321", "2000", "4128", "test", "shit", "love",
                    "baby", "cunt", "mark", "3333", "john", "sexy", "5150",
                    "4444", "2112", "fred", "mike", "1111", "tits", "paul",
                    "mine", "king", "fire", "5555", "slut", "girl", "2222",
                    "asdf", "time", "7777", "rock", "xxxx", "ford", "dick",
                    "bill", "wolf", "blue", "alex", "cock", "beer", "eric",
                    "6666", "jack" },
            // #5 characters
            { "beach", "great", "black", "pussy", "12345", "frank", "tiger",
                    "japan", "money", "naked", "11111", "angel", "stars",
                    "apple", "porno", "steve", "viper", "horny", "ou812",
                    "kevin", "buddy", "teens", "young", "jason", "lucky",
                    "girls", "lover", "brian", "kitty", "bubba", "happy",
                    "cream", "james", "xxxxx", "booty", "kelly", "boobs",
                    "penis", "eagle", "white", "enter", "chevy", "smith",
                    "chris", "green", "sammy", "super", "magic", "power",
                    "enjoy", "scott", "david", "video", "qwert", "paris",
                    "women", "juice", "dirty", "music", "peter", "bitch",
                    "house", "hello", "billy", "movie" },
            // #6 characters
            { "123456", "prince", "guitar", "butter", "jaguar", "united",
                    "turtle", "muffin", "cooper", "nascar", "redsox", "dragon",
                    "zxcvbn", "qwerty", "tomcat", "696969", "654321", "murphy",
                    "987654", "amanda", "brazil", "wizard", "hannah", "lauren",
                    "master", "doctor", "eagle1", "gators", "squirt", "shadow",
                    "mickey", "mother", "monkey", "bailey", "junior", "nathan",
                    "abc123", "knight", "alexis", "iceman", "fuckme", "tigers",
                    "badboy", "bonnie", "purple", "debbie", "angela", "jordan",
                    "andrea", "spider", "harley", "ranger", "dakota", "booger",
                    "iwantu", "aaaaaa", "lovers", "player", "flyers", "suckit",
                    "hunter", "beaver", "morgan", "matrix", "boomer", "runner",
                    "batman", "scooby", "edward", "thomas", "walter", "helpme",
                    "gordon", "tigger", "jackie", "casper", "robert", "booboo",
                    "boston", "monica", "stupid", "access", "coffee", "braves",
                    "xxxxxx", "yankee", "saturn", "buster", "gemini", "barney",
                    "apples", "soccer", "rabbit", "victor", "august", "hockey",
                    "peanut", "tucker", "killer", "canada", "george", "johnny",
                    "sierra", "blazer", "andrew", "spanky", "doggie", "232323",
                    "winter", "zzzzzz", "brandy", "gunner", "beavis", "compaq",
                    "horney", "112233", "carlos", "arthur", "dallas", "tennis",
                    "sophie", "ladies", "calvin", "shaved", "pepper", "giants",
                    "surfer", "fender", "samson", "austin", "member", "blonde",
                    "blowme", "fucked", "daniel", "donald", "golden", "golfer",
                    "cookie", "summer", "bronco", "racing", "sandra", "hammer",
                    "pookie", "joseph", "hentai", "joshua", "diablo", "birdie",
                    "maggie", "sexsex", "little", "biteme", "666666", "topgun",
                    "ashley", "willie", "sticky", "cowboy", "animal", "silver",
                    "yamaha", "qazwsx", "fucker", "justin", "skippy", "orange",
                    "banana", "lakers", "marvin", "merlin", "driver", "rachel",
                    "marine", "slayer", "angels", "asdfgh", "bigdog", "vagina",
                    "apollo", "cheese", "toyota", "parker", "maddog", "travis",
                    "121212", "london", "hotdog", "wilson", "sydney", "martin",
                    "dennis", "voodoo", "ginger", "magnum", "action", "nicole",
                    "carter", "erotic", "sparky", "jasper", "777777", "yellow",
                    "smokey", "dreams", "camaro", "xavier", "teresa", "freddy",
                    "secret", "steven", "jeremy", "viking", "falcon", "snoopy",
                    "russia", "taylor", "nipple", "111111", "eagles", "131313",
                    "winner", "tester", "123123", "miller", "rocket", "legend",
                    "flower", "theman", "please", "oliver", "albert" },
            // #7 characters
            { "porsche", "rosebud", "chelsea", "amateur", "7777777", "diamond",
                    "tiffany", "jackson", "scorpio", "cameron", "testing",
                    "shannon", "madison", "mustang", "bond007", "letmein",
                    "michael", "gateway", "phoenix", "thx1138", "raiders",
                    "forever", "peaches", "jasmine", "melissa", "gregory",
                    "cowboys", "dolphin", "charles", "cumshot", "college",
                    "bulldog", "1234567", "ncc1701", "gandalf", "leather",
                    "cumming", "hunting", "charlie", "rainbow", "asshole",
                    "bigcock", "fuckyou", "jessica", "panties", "johnson",
                    "naughty", "brandon", "anthony", "william", "ferrari",
                    "chicken", "heather", "chicago", "voyager", "yankees",
                    "rangers", "packers", "newyork", "trouble", "bigtits",
                    "winston", "thunder", "welcome", "bitches", "warrior",
                    "panther", "broncos", "richard", "8675309", "private",
                    "zxcvbnm", "nipples", "blondes", "fishing", "matthew",
                    "hooters", "patrick", "freedom", "fucking", "extreme",
                    "blowjob", "captain", "bigdick", "abgrtyu", "chester",
                    "monster", "maxwell", "arsenal", "crystal", "rebecca",
                    "pussies", "florida", "phantom", "scooter", "success" },
            // #8 characters
            { "firebird", "password", "12345678", "steelers", "mountain",
                    "computer", "baseball", "xxxxxxxx", "football", "qwertyui",
                    "jennifer", "danielle", "sunshine", "starwars", "whatever",
                    "nicholas", "swimming", "trustno1", "midnight", "princess",
                    "startrek", "mercedes", "superman", "bigdaddy", "maverick",
                    "einstein", "dolphins", "hardcore", "redwings", "cocacola",
                    "michelle", "victoria", "corvette", "butthead", "marlboro",
                    "srinivas", "internet", "redskins", "11111111", "access14",
                    "rush2112", "scorpion", "iloveyou", "samantha", "mistress" } };
}
