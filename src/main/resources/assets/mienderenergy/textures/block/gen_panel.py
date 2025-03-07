import random
from PIL import Image, ImageDraw

LEVELS = {
    "lv": (100, 150, 255),  # Light Blue
    "mv": (50, 200, 255),   # Cyan
    "hv": (30, 80, 200),    # Deep Blue
    "ev": (80, 50, 200),    # Purple-Blue
    "iv": (255, 80, 80)     # Reddish
}


def generate_grayscale_pattern(size=16):
    img = Image.new("L", (size, size), 100)  # Base gray color
    draw = ImageDraw.Draw(img)

    # Draw a grid pattern
    for x in range(0, size, 4):
        draw.line((x, 0, x, size), fill=150, width=1)
    for y in range(0, size, 4):
        draw.line((0, y, size, y), fill=150, width=1)
    
    return img

def add_random_noise(img, intensity=20):
    noise_img = img.copy()
    pixels = noise_img.load()

    for x in range(img.width):
        for y in range(img.height):
            noise = random.randint(-intensity, intensity)
            new_value = max(0, min(255, pixels[x, y] + noise))
            pixels[x, y] = new_value

    return noise_img

def apply_blue_tint(grayscale_img, tint_color):
    img_rgb = grayscale_img.convert("RGB")
    pixels = img_rgb.load()
    
    for x in range(img_rgb.width):
        for y in range(img_rgb.height):
            gray = pixels[x, y][0]
            blended = (
                min(255, int(gray * 0.3 + tint_color[0] * 0.7)),
                min(255, int(gray * 0.3 + tint_color[1] * 0.7)),
                min(255, int(gray * 0.3 + tint_color[2] * 0.7))
            )
            pixels[x, y] = blended

    return img_rgb

for level, color in LEVELS.items():
    grayscale_texture = generate_grayscale_pattern()
    noisy_texture = add_random_noise(grayscale_texture, intensity=20)
    tinted_texture = apply_blue_tint(noisy_texture, color)

    filename = f"solar_panel_{level}_block.png"
    tinted_texture.save(filename)
    print(f"Generated: {filename}")
