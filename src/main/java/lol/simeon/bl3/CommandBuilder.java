package lol.simeon.bl3;

public class CommandBuilder {

  private final CommandPrefixes commandPrefix;
  private final StringBuilder stringBuilder;

  public CommandBuilder(CommandPrefixes commandPrefix) {
    this.commandPrefix = commandPrefix;
    this.stringBuilder = new StringBuilder(commandPrefix.getPrefix());
  }

  public CommandBuilder replaceNextInt(int value) {
    int index = stringBuilder.indexOf("!");
    if(index == -1) {
      throw new IllegalArgumentException("No n found in command prefix");
    }
    stringBuilder.replace(index, index + 1, String.valueOf(value));
    return this;
  }

  public CommandBuilder replaceNextInt(String value){
    int index = stringBuilder.indexOf("!");
    if(index == -1) {
      throw new IllegalArgumentException("No ! found in command prefix");
    }
    stringBuilder.replace(index, index + 1, value);
    return this;
  }

  public CommandBuilder replaceNextString(String value) {
    int index = stringBuilder.indexOf("@");
    if (index == -1) {
      throw new IllegalArgumentException("No $ found in command prefix");
    }
    stringBuilder.replace(index, index + 2, value);
    return this;
  }

  public CommandBuilder addAnotherCommand(CommandPrefixes commandPrefix) {
    stringBuilder.append(" ");
    stringBuilder.append(commandPrefix.getPrefix());
    return this;
  }

  public String build() {
    /*for (int i = 0; i < stringBuilder.length(); i++) {
      stringBuilder.insert(i, " ");
      i++;
    }*/
    return stringBuilder.toString();
  }



}
