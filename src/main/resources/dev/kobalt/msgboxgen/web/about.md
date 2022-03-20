### Purpose

This program generates a message box with message and buttons with classic Redmond-like style.

### Mechanism

Submitted data is used to render a simulated message box window.

Background and title bar is rendered based on submitted colors. Icon is positioned to the left side. Text is measured to
fit within given width and it's rendered with given font. Buttons are positioned under the text.

Final render as a response is a PNG image file.

### Limitations

- Only up to 3 buttons can be rendered properly.
- Maximum amount of characters in title and message is 512.
- Font size cannot be changed.
- Window width can be set up to 720px.
- External resources for fonts and icons are required for rendering text and icon.