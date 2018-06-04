import matplotlib.pyplot as plt
import os
import sys
import numpy as np

# check if dir was provided

if (len(sys.argv) < 1):
    print 'missing parameter directory'
    sys.exit()

reverse = 0
if (len(sys.argv) > 2):
    reverse = sys.argv[2]



# List all files from input path

mypath = sys.argv[1]
onlyfiles = [f for f in os.listdir(mypath) if os.path.isfile(os.path.join(mypath, f))]
print 'Loading '+str(len(onlyfiles))+' files...'
print ''



# Create lists

sa_gen = []
sa_best = []
sa_timer = []



# Read files

for file_name in onlyfiles:

    if (file_name.find('.stat') < 0):
        continue;

    # Read data
    data = open(mypath+file_name)

    gen = []
    best = []
    timer = []

    for line in data:
        line = line.replace('\n', '')
        aux = line.split(' ')
        gen.append(aux[0])
        timer.append(aux[len(aux)-1])
        if (reverse):
            best.append(-aux[1])
        else:
            best.append(aux[1])

    # Store in Simulated Annealing lists
    if (file_name.find('_SA.stat') > 0):

        sa_gen.append(gen)
        sa_best.append(best)
        sa_timer.append(timer)



# Convert all lists to arrays
sa_best = np.asarray(sa_best, 'int')
sa_gen = np.asarray(sa_gen, 'int')
sa_timer = np.asarray(sa_timer, 'int')

# Statistics
sa_mean = np.mean(sa_best[:,-1])
sa_std = np.std(sa_best[:,-1])
sa_min = np.min(sa_best[:,-1])
sa_max = np.max(sa_best[:,-1])
sa_fit = np.argmin(sa_best[:,-1])
sa_meant = np.mean(sa_timer[:,-1])
sa_stdt = np.std(sa_timer[:,-1])
sa_mint = np.min(sa_timer[:,-1])
sa_maxt = np.max(sa_timer[:,-1])



# PLOT Best Solution
plt.subplot(2,2,1)
for i in range(len(sa_best)):
    plt.plot(sa_gen[i], sa_best[i], '-', color='lightblue', alpha=0.2)
# Best Solution
plt.plot(sa_gen[sa_fit], sa_best[sa_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa_gen[sa_fit], sa_best[sa_fit], '-', color='blue', alpha=0.75, linewidth=3)
# Plot details
plt.ylim(0, sa_mean * 2)
plt.xlabel('generation')
plt.ylabel('fitness of best solution (days)')
leg = plt.legend(['Simulated Annealing'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)

# PLOT Best Solution Statistics
plt.subplot(2,2,2)
# Plot details
plt.ylim(0, sa_mean * 2)
box = plt.boxplot(sa_best[:,-1], vert=True, patch_artist=True, widths=0.5)
box['boxes'][0].set_facecolor('lightblue')
leg = plt.legend(['Simulated Annealing'])
leg.legendHandles[0].set_marker('s')
leg.legendHandles[0].set_color('lightblue')


# PLOT Timer
plt.subplot(2,2,3)
for i in range(len(sa_timer)):
    plt.plot(sa_gen[i], sa_timer[i], '-', color='lightblue', alpha=0.2)
# Best Solution Timer
plt.plot(sa_gen[sa_fit], sa_timer[sa_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa_gen[sa_fit], sa_timer[sa_fit], '-', color='blue', alpha=0.75, linewidth=3)
# Plot details
plt.ylim(0, (sa_meant * 2))
plt.xlabel('generation')
plt.ylabel('time spent (ns)')
leg = plt.legend(['Simulated Annealing'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)

# PLOT Timer Statistics
plt.subplot(2,2,4)
# Plot details
plt.ylim(0, (sa_meant * 2))
box = plt.boxplot(sa_timer[:,-1], vert=True, patch_artist=True, widths=0.5)
box['boxes'][0].set_facecolor('lightblue')
leg = plt.legend(['Simulated Annealing'])
leg.legendHandles[0].set_marker('s')
leg.legendHandles[0].set_color('lightblue')

# Print results before showing plot
print ''
print '>>> RESULTS - Mission Duration (days) <<<'
print ''
print 'Simulated Annealing:'
print '    Best Solution      = '+str(sa_best[sa_fit][-1])
print '    Mean of Solutions  = '+str(sa_mean)+" ["+str(sa_min)+"-"+str(sa_max)+"]"
print '    Standard Deviation = '+str(sa_std)
print ''
print ''
print '>>> RESULTS - Optimization Time (ns) <<<'
print ''
print 'Simulated Annealing:'
print '    Best Solution      = '+str(sa_timer[sa_fit][-1])
print '    Mean of Solutions  = '+str(sa_meant)+" ["+str(sa_mint)+"-"+str(sa_maxt)+"]"
print '    Standard Deviation = '+str(sa_stdt)
print ''

# PLOT Save
plt.savefig(mypath+'plot.png')

# PLOT Show
#plt.show()