package dreipc.asset.search.utils;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Accessors(fluent = true)
@Slf4j
public class PrettyLogPrinter {

  private static Marker noNewLineMarker = MarkerFactory.getMarker("NO_NEW_LINE");
  @Setter
  private long startTime;
  @Setter
  private long totalCount;

  public void printProgress(long currentCount) {
    long eta = currentCount == 0 ? 0
        : (totalCount - currentCount) * (System.currentTimeMillis() - startTime) / currentCount;

    String etaHms = currentCount == 0 ? "N/A" : String
        .format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
            TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

    StringBuilder string = new StringBuilder(140);
    int percent = (int) (currentCount * 100 / totalCount);
    if (currentCount <= totalCount) {
      string.append("\r")
          .append(String.join("",
              Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
          .append(String.format(" %d%% [", percent))
          .append(String.join("", Collections.nCopies(percent, "=")))
          .append('>')
          .append(String.join("", Collections.nCopies(100 - percent, " ")))
          .append(']')
          .append(String.join("", Collections.nCopies(
              currentCount == 0 ? (int) (Math.log10(totalCount))
                  : (int) (Math.log10(totalCount)) - (int) (Math.log10(currentCount)), " ")))
          .append(String.format(" %d/%d, ETA: %s", currentCount, totalCount, etaHms));
    }

    System.out.print(string);
    //			log.info(noNewLineMarker, string.toString());

    if ((currentCount - totalCount) == 0) {
      System.out.print("\n\n");
    }
    //			log.info(noNewLineMarker, "\n\n");
  }
}
