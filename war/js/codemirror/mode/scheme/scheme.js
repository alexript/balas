CodeMirror.defineMode("scheme", function() {
	var isWhiteSpace = function(ch) {
		// The messy regexp is because IE's regexp matcher is of the
		// opinion that non-breaking spaces are no whitespace.
		return ch != "\n" && /^[\s\u00a0]*$/.test(ch);
	};

	var whitespaceChar = new RegExp("[\\s\\u00a0]");

	var readWhitespace = function(stream) {
		stream.eatWhile(isWhiteSpace);
		var content = stream.peek();
		return 'whitespace';
	};

	var readPound = function(source) {
		var text;
		// FIXME: handle special things here
		if (source.equals(";")) {
			source.next();
			text = source.get();
			return "scheme-symbol";
		} else {
			text = source.get();

			return "scheme-symbol";
		}

	};

	var scanUntilEndline = function(source, end) {
		while (!source.endOfLine()) {
			source.next();
		}
	}

	var readLineComment = function(source) {
		scanUntilEndline(source);
		var text = source.get();
		return "scheme-comment";
	};

	// scanUntilUnescaped: string-stream char -> boolean
	// Advances the stream until the given character (not preceded by a
	// backslash) is encountered.
	// Returns true if we hit end of line without closing.
	// Returns false otherwise.
	var scanUntilUnescaped = function(source, end) {
		var escaped = false;
		while (true) {
			if (source.eol()) {
				return true;
			}
			var next = source.next();
			if (next == end && !escaped)
				return false;
			escaped = !escaped && next == "\\";
		}
		return false;
	}

	var readString = function(quote, source) {
		var isUnclosedString = scanUntilUnescaped(source, quote);
		if (isUnclosedString) {
			// setState(UNCLOSED_STRING);
		}
		var word = source.peek();
		return 'scheme-string';
	};

	var isDelimiterChar = new RegExp(
			"[\\s\\\(\\\)\\\[\\\]\\\{\\\}\\\"\\\,\\\'\\\`\\\;]");
	var isNotDelimiterChar = new RegExp(
			"[^\\s\\\(\\\)\\\[\\\]\\\{\\\}\\\"\\\,\\\'\\\`\\\;]");
	var numberHeader = ("(?:(?:\\d+\\/\\d+)|"
			+ ("(?:(?:\\d+\\.\\d+|\\d+\\.|\\.\\d+)(?:[eE][+\\-]?\\d+)?)|")
			+ ("(?:\\d+(?:[eE][+\\-]?\\d+)?))"));
	var numberPatterns = [
			// complex numbers
			new RegExp("^((?:(?:\\#[ei])?[+\\-]?" + numberHeader + ")?"
					+ "(?:[+\\-]" + numberHeader + ")i$)"),
			/^((?:\#[ei])?[+-]inf.0)$/, /^((?:\#[ei])?[+-]nan.0)$/,
			new RegExp("^((?:\\#[ei])?[+\\-]?" + numberHeader + "$)"),
			new RegExp("^0[xX][0-9A-Fa-f]+$") ];

	// looksLikeNumber: string -> boolean
	// Returns true if string s looks like a number.
	var looksLikeNumber = function(s) {
		for ( var i = 0; i < numberPatterns.length; i++) {
			if (numberPatterns[i].test(s)) {
				return true;
			}
		}
		return false;
	};

	var readWordOrNumber = function(source) {
		source.eatWhile(isNotDelimiterChar);
		var word = source.peek();
		if (looksLikeNumber(word)) {
			return "scheme-number";
		} else {
			return "scheme-symbol";
		}
	};

	return {
		token : function(stream) {
			var ch = stream.next();
			if (ch === '\n') {
				var content = stream.get();
				return 'whitespace';
			} else if (whitespaceChar.test(ch)) {
				return readWhitespace(stream);
			} else if (ch === "#") {
				return readPound(stream);
			} else if (ch === ';') {
				return readLineComment(stream);
			} else if (ch === "\"") {
				return readString(ch, stream);
			} else if (isDelimiterChar.test(ch)) {
				return "scheme-punctuation";
			} else {
				return readWordOrNumber(stream);
			}
		}
	};
});

CodeMirror.defineMIME("text/scheme", "scheme");
